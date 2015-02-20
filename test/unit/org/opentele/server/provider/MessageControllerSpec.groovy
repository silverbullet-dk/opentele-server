package org.opentele.server.provider
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.core.MessageService
import org.opentele.server.model.*
import org.opentele.server.provider.model.MessageController
import org.springframework.security.core.Authentication
import spock.lang.Specification

@TestFor(MessageController)
@Build([Patient, User, PatientGroup, Patient2PatientGroup, Department, Message])
class MessageControllerSpec extends Specification {
    Patient patient
    MessageService messageService
    SessionService sessionService

    def setup() {
        // Ugly hack to avoid a null pointer exception, since the springSecurityService can not be injected
        User.metaClass.encodePassword = {-> }

        patient = createPatient()

        Authentication authentication = Mock(Authentication)
        authentication.authenticated >> true

        messageService = Mock(MessageService)
        controller.messageService = messageService

        sessionService = Mock(SessionService)
        controller.sessionService = sessionService

        controller.springSecurityService = Mock(SpringSecurityService)
        //controller.springSecurityService.currentUser >> patient.user
        controller.springSecurityService.authentication >> authentication
    }

    def 'inserts Re: when creating a message reply'() {
        setup:
        Department department = Department.build(name: "Ward")
        def message = Message.build(department: department, patient: patient, title: 't', text: 'T', sentByPatient: true)

        when:
        params.id = message.id
        def model = controller.reply()

        then:
        model.messageInstance.title.equals("Re: t")
    }

    def 'does not insert a duplicate Re: if one is already at start of message'() {
        setup:
        Department department = Department.build(name: "Ward")
        def message = Message.build(department: department, patient: patient, title: 'Re: stuff', text: 'T', sentByPatient: true)

        when:
        params.id = message.id
        def model = controller.reply()

        then:
        model.messageInstance.title.equals("Re: stuff")
    }

    private createPatient() {
        def user = User.build(password: "password1", cleartextPassword: null)
        user.save(validate: false)
        return Patient.build(user: user)
    }
}
