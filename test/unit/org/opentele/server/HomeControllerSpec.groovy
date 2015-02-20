package org.opentele.server

import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.provider.HomeController
import org.opentele.server.provider.SessionService
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.User
import org.opentele.server.core.model.types.PermissionName;
import spock.lang.Specification;

@TestFor(HomeController)
@Build([User, Patient, Clinician])
public class HomeControllerSpec extends Specification {
    User user

    def setup() {
        user = User.build(password: 'abcd1234', springSecurityService: Mock(SpringSecurityService))

        controller.sessionService = Mock(SessionService)
        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.getCurrentUser() >> user
    }

    def 'logs user out if user has no permission to log in'() {
        setup:
        controller.sessionService.hasPermission(_) >> false

        when:
        controller.index()

        then:
        response.redirectedUrl == '/j_spring_security_logout'
    }

    def 'redirects to questionnaire overview if user has no clinician or patient associated'() {
        setup:
        controller.sessionService.hasPermission(PermissionName.WEB_LOGIN) >> true

        when:
        controller.index()

        then:
        response.redirectedUrl.endsWith '/questionnaire/list'
    }

    def 'redirects to patient overview if user has clinician associated and has permission to see patient overview'() {
        setup:
        controller.sessionService.hasPermission(PermissionName.WEB_LOGIN) >> true
        controller.sessionService.hasPermission(PermissionName.PATIENT_READ_ALL) >> true
        user.metaClass.isClinician = { -> true }

        when:
        controller.index()

        then:
        response.redirectedUrl.endsWith '/patientOverview'
    }

    def 'redirects to questionnaire overview if user has clinician associated, but has no permission to see patient overview'() {
        setup:
        controller.sessionService.hasPermission(PermissionName.WEB_LOGIN) >> true
        user.metaClass.isClinician = { -> true }

        when:
        controller.index()

        then:
        response.redirectedUrl.endsWith '/questionnaire/list'
    }

    def 'redirects to patient questionnaire view if user has patient associated'() {
        setup:
        controller.sessionService.hasPermission(PermissionName.WEB_LOGIN) >> true
        user.metaClass.isPatient = { -> true }

        when:
        controller.index()

        then:
        response.redirectedUrl.endsWith '/patient/questionnaires'
    }
}
