package org.opentele.server.model

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.model.patientquestionnaire.PatientHelpInfo
import org.opentele.server.model.questionnaire.HelpInfo
import org.opentele.server.util.HelpImageUtil
import org.springframework.dao.DataIntegrityViolationException

import javax.imageio.ImageIO

@Secured(PermissionName.NONE)
class HelpImageController {
    // Inject spring security service
    def springSecurityService

    static allowedMethods = [save: "POST"/*, update: "POST", delete: "POST"*/]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.PATIENT_QUESTIONNAIRE_READ_ALL)
    def list() {
//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [helpImageInstanceList: HelpImage.list(params), helpImageInstanceTotal: HelpImage.count()]
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def create() {
//        [helpImageInstance: new HelpImage(params)]
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def upload() {
        def file = request.getFile('file')
        if(file.empty) {
            flash.error = message(code: 'helpImage.fileempty.error')
        } else if (!grailsApplication.config.help.image.contentTypes.contains(file.getContentType())) {
            flash.error = message(code: 'helpImage.contenttype.error', args: [file.getContentType(), grailsApplication.config.help.image.contentTypes])
        }
        else {
            try {
                def helpImageInstance = new HelpImage(params)
                helpImageInstance.filename = file.originalFilename
                def fullPath = HelpImageUtil.getAndEnsureUploadDir() + helpImageInstance.filename
                def destFile = new File(fullPath)
                if (destFile.exists()) {
                    flash.error = message(code: 'helpImage.fileexists.error', args: [helpImageInstance.filename])
                } else {
                    file.transferTo(new File(fullPath))

                    //check size
                    def img = ImageIO.read(new File(fullPath))
                    int imgW = img.getWidth()
                    int imgH = img.getHeight()

                    if (imgW > 2000 || imgH > 2000) {
                        flash.error = message(code: 'helpImage.size.error')
                        (new File(fullPath)).delete()
                    } else {
                        helpImageInstance.save()
                    }
                }
            } catch (e) {
                flash.error = message(code: 'helpImage.upload.error')
                log.warn("Exception when uploading image file: ${e.message}", e)
            }
        }
        redirect (action:'list')
    }

    @Secured(PermissionName.PATIENT_QUESTIONNAIRE_READ_ALL)
    def downloadImage() {
        HelpImage helpImageInstance = HelpImage.get(params.id)
        if ( helpImageInstance == null) {
            flash.error = message(code: 'helpImage.exists.error')
            redirect (action:'list')
        } else {
            try {
                response.setContentType("APPLICATION/OCTET-STREAM")
                response.setHeader("Content-Disposition", "Attachment;Filename=\"${helpImageInstance.filename}\"")

                def file = new File(HelpImageUtil.getAndEnsureUploadDir(), helpImageInstance.filename)
                log.debug("Trying to load help image from: " + file.absolutePath)
                def fileInputStream = new FileInputStream(file)
                def outputStream = response.getOutputStream()

                byte[] buffer = new byte[4096];
                int len;
                while ((len = fileInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.flush()
                outputStream.close()
                fileInputStream.close()
            } catch (e) {
                flash.error = message(code: 'helpImage.download.error')
                log.warn("Exception when download image file: ${e.message}", e)
                redirect (action:'list')
            }
        }
    }

    @Secured(PermissionName.PATIENT_QUESTIONNAIRE_READ_ALL)
    def show() {
        def helpImageInstance = HelpImage.get(params.id)
        if (!helpImageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'helpImage.shortname', default: 'HelpImage'), params.id])
            redirect(action: "list")
            return
        }

        [helpImageInstance: helpImageInstance]
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def delete() {
        def helpImageInstance = HelpImage.get(params.id)
        if (!helpImageInstance) {
            flash.error = message(code: 'default.not.found.message', args: [message(code: 'helpImage.shortname', default: 'HelpImage'), params.id])
            redirect(action: "list")
            return
        }

        try {
            //Check for references:
            Boolean hasReferences = false
            String errMsg = message(code: 'helpImage.delete.reference.error')
            def helpInfos = HelpInfo.findAllByHelpImage(helpImageInstance)
            if (helpInfos) {
                hasReferences = true
                errMsg += message(code: 'questionnaireHeader.draftQuestionnaires.label') + ": "
                helpInfos.each {
                    errMsg += it?.questionnaireNode?.questionnaire?.name + "(ver. " +
                            it?.questionnaireNode?.questionnaire?.version + ")"
                    if (it != helpInfos.last())
                        errMsg += ", "
                    else
                        errMsg += "."
                }
            }
            def patientHelpInfos = PatientHelpInfo.findAllByHelpImage(helpImageInstance)
            if (patientHelpInfos) {
                hasReferences = true
                errMsg += "\n" + message(code: 'questionnaireHeader.publishedQuestionnaires.label') + ": "
                patientHelpInfos.each {
                    errMsg += it?.questionnaireNode?.questionnaire?.name + "(ver. " +
                            it?.questionnaireNode?.questionnaire?.version + ")"
                    if (it != patientHelpInfos.last())
                        errMsg += ", "
                    else
                        errMsg += "."
                }
            }
            if (hasReferences) {
                flash.error = errMsg
                redirect(action: "list")
            } else {
                //Delete file:
                def fullPath  = HelpImageUtil.getAndEnsureUploadDir() + helpImageInstance.filename
                def file = new File(fullPath)
                if (file.exists())
                    file.delete()

                //Delete instance:
                helpImageInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'helpImage.shortname', default: 'HelpImage'), params.id])
                redirect(action: "list")
            }
        }
        catch (DataIntegrityViolationException e) {
            flash.error = message(code: 'default.not.deleted.message', args: [message(code: 'helpImage.shortname', default: 'HelpImage'), params.id])
            redirect(action: "show", id: params.id)
        } catch (e) {
            flash.error = message(code: 'default.not.deleted.message', args: [message(code: 'helpImage.shortname', default: 'HelpImage'), params.id])
            log.warn("Exception when deleting image file: ${e.message}", e)
            redirect(action: "show")
        }
    }
}
