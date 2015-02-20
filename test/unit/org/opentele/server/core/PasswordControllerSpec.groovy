package org.opentele.server.core

import grails.test.mixin.TestFor
import org.opentele.server.core.PasswordCommand
import org.opentele.server.core.PasswordController
import org.opentele.server.core.PasswordService
import org.opentele.server.model.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PasswordController)
class PasswordControllerSpec extends Specification {

    def setup() {
        controller.passwordService = Mock(PasswordService)
    }

    def "when opening change the model should return an empty command object" () {
        when:
        def model = controller.change()

        then:
        model.command
        !model.command.password
        !model.command.currentPassword
        !model.command.passwordRepeat
    }

    def "when posting a password change with no errors a redirect to changed should be seen"() {
        setup:
        def command = Mock(PasswordCommand)
        controller.passwordService.changePassword(command)
        command.hasErrors() >> false
        command.user >> new User(username: "kryf")
        request.method = 'POST'

        when:
        controller.update(command)

        then:
        flash.message == "password.changed.for.user"
        response.redirectedUrl == "/password/changed"
    }
    def "when posting a password change with errors the view should be redrawn"() {
        setup:
        def command = Mock(PasswordCommand)
        controller.passwordService.changePassword(command)
        command.hasErrors() >> true
        request.method = 'POST'

        when:
        controller.update(command)

        then:
        view == "/password/change"
        model.command == command
    }
}
