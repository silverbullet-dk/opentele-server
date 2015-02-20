package org.opentele.server.provider

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.core.model.types.PermissionName

@Secured("IS_AUTHENTICATED_FULLY")
class HomeController {
    def sessionService
    def springSecurityService

    /**
     * Redirects based on role.
     */
    @Secured(["IS_AUTHENTICATED_REMEMBERED"])
    def index() {
		//Clear the selected patient if any
		sessionService.setNoPatient(session)
		
        if (!sessionService.hasPermission(PermissionName.WEB_LOGIN)) {
            log.debug "Everybody else"
            redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl
            return
        }

        def user = springSecurityService.currentUser
        if (user.isClinician() && sessionService.hasPermission(PermissionName.PATIENT_READ_ALL)) {
            log.debug 'Redirecting to clinician view'
            redirect(controller: "patientOverview")
        } else if (user.isPatient()) {
            log.debug 'Redirecting to patient view'
            redirect(controller: "patient", action: "questionnaires")
        } else {
            log.debug 'Redirecting to administrator view'
            redirect(controller: "questionnaire", action: "list")
        }
    }
}
