package org.opentele.server
import grails.plugins.artefactmessaging.ArtefactMessagingService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.web.util.WebUtils

import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.User

class SecurityFilters {

    def patientService
    def sessionService
    def springSecurityService
    ArtefactMessagingService artefactMessagingService
    def grailsApplication

    def filters = {

        primarySecurityFilter (controller: '*', action: '*') {

            after = { Map model ->
                String patientId = session[Constants.SESSION_PATIENT_ID]
                log.debug "CPR set in session: ${patientId}  "

                if (patientId && !patientId.equals("")) {
                    def pId = Long.valueOf(patientId)

                    def allowedToView = patientService.allowedToView(pId)
                    log.debug "Allowed to View: " + allowedToView

                    if (!allowedToView) {
                        log.debug "Setting view to noAccess"
                        sessionService.setNoPatient(session)
                        redirect(controller: 'meta', action: "noAccess")
                    }
                }
            }
        }

        changePasswordFilter(controller: '*', action: '*') {
            before = {
                if(request.xhr || controllerName in ['login','logout','password']) return true

                User user = springSecurityService.currentUser as User
                if(user?.cleartextPassword) {
                    def message = artefactMessagingService.getMessage(code: "password.must.be.changed")
                    flash.message = message
                    redirect(controller: 'password', action: 'change')
                    return false
                }
            }
        }

        clincianOnlyFilter(uri: '/**') {
            before = {
                if (controllerName in ['login', 'logout', 'password', 'meta', 'videoResource']) return true

                def contextPath = applicationContext.grailsApplication.mainContext.servletContext.contextPath
                def urlRequested = WebUtils.getForwardURI(request) - contextPath

                User user = springSecurityService.currentUser as User
                if (user.isClinician()) return true
                redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
                return false
            }
        }
    }
}
