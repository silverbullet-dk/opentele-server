package org.opentele.server.provider.model.questionnaire

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class QuestionnaireHeaderController {
    def questionnaireHeaderService
    def clinicianService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.sort = params.sort ?: 'name'
        params.order = params.order ?: 'asc'
        [questionnaireHeaderInstanceList: QuestionnaireHeader.list(params), questionnaireHeaderInstanceTotal: QuestionnaireHeader.count()]
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def create() {
        [questionnaireHeaderInstance: new QuestionnaireHeader(params)]
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def save(String name) {
        def questionnaireHeaderInstance = new QuestionnaireHeader(name: name.trim())
        if (!questionnaireHeaderInstance.save(flush: true)) {
            render(view: "create", model: [questionnaireHeaderInstance: questionnaireHeaderInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), questionnaireHeaderInstance.id])
        redirect(action: "show", id: questionnaireHeaderInstance.id)
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def saveAndEdit(String name) {
        def questionnaireHeaderInstance = new QuestionnaireHeader(name: name.trim())
        if (!questionnaireHeaderInstance.save(flush: true)) {
            render(view: "create", model: [questionnaireHeaderInstance: questionnaireHeaderInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), questionnaireHeaderInstance.id])
        redirect(action: "doCreateDraft", id: questionnaireHeaderInstance.id)
    }


    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
    def show(Long id) {
        withInstance(id) { QuestionnaireHeader questionnaireHeader ->
            [questionnaireHeaderInstance: questionnaireHeader, historicQuestionnaires: questionnaireHeaderService.findHistoricQuestionnaires(questionnaireHeader)]
        }
    }



    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def edit(Long id) {
        withInstance(id) {
            [questionnaireHeaderInstance: it]
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def update() {
        Long id = params.long('id')
        String name = params.name.trim()
        boolean requiresManualInspection = (params.requiresManualInspection != null)

        withInstance(id) {
            if (params.version) {
                def version = params.version.toLong()
                if (it.version > version) {
                    it.errors.rejectValue("version", "default.optimistic.locking.failure",
                            [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader')] as Object[],
                            "Another user has updated this QuestionnaireHeader while you were editing")
                    render(view: "edit", model: [questionnaireHeaderInstance: questionnaireHeaderInstance])
                    return
                }
            }

            // TODO: Service that renames both QuestionnaireHeader and Questionnaire names
            it.name = name

            it.requiresManualInspection = requiresManualInspection

            if (!it.save(flush: true)) {
                render(view: "edit", model: [questionnaireHeaderInstance: it])
                return
            }

            flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), it.id])
            redirect(action: "show", id: it.id)
        }
    }


    @Secured(PermissionName.QUESTIONNAIRE_DELETE)
    def unpublish(Long id) {
        withInstance(id) {  QuestionnaireHeader questionnaireHeader ->
            questionnaireHeaderService.unpublishActive(questionnaireHeader)
            flash.message = message(code: 'questionnaireHeader.activeQuestionnaire.removed')
            redirect(action: 'show', id: id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def publishDraft(Long id) {
        withInstance(id) { QuestionnaireHeader questionnaireHeader ->
            try {
                questionnaireHeaderService.publishDraft(questionnaireHeader, clinicianService.currentClinician)
                flash.message = message(code: 'questionnaireHeader.draftPublished')
            } catch (Exception e) {
                log.error('Could not publish draft', e)
                flash.error = message(code: 'questionnaireHeader.draftPublished.error')
            }
            redirect(action: 'show', id: id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def deleteDraft(Long id) {
        withInstance(id) {  QuestionnaireHeader questionnaireHeader ->
            questionnaireHeaderService.deleteDraft(questionnaireHeader)
            flash.message = message(code: 'questionnaireHeader.draftDeleted')
            redirect(action: 'show', id: id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_DELETE)
    def delete(Long id) {
        withInstance(id) { QuestionnaireHeader questionnaireHeader ->
            questionnaireHeaderService.delete(questionnaireHeader)
            flash.message = message(code: 'questionnaireHeader.deleted')
            redirect(action: 'list')
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def editDraft(Long id) {
        withInstance(id) {
            redirect(controller: "questionnaireEditor", action: "edit", id: id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def createDraft(Long id) {
        withInstance(id) {
            if(it.questionnaires) {
                [questionnaireHeaderInstance: it]
            } else {
                redirect(controller: "questionnaireEditor", action: "edit", id: id)
            }
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def doCreateDraft(Long id, Long baseId) {
        withInstance(id) {
            if(baseId) {
                redirect(controller: "questionnaireEditor", action: "edit", id: id, params: [baseId: baseId])
            } else {
                redirect(controller: "questionnaireEditor", action: "edit", id: id)
            }
        }
    }


    private withInstance(Long id, Closure closure) {
        def questionnaireHeaderInstance = QuestionnaireHeader.get(id)
        if (questionnaireHeaderInstance) {
            return closure.call(questionnaireHeaderInstance)
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), id])
            redirect(action: "list")
        }

    }


}
