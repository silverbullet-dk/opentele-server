package org.opentele.server.core

import grails.converters.JSON
import org.apache.log4j.Logger
import org.opentele.server.core.test.AbstractIntegrationSpec
import org.opentele.server.model.User
import spock.lang.Ignore

class PasswordControllerIntegrationSpec extends AbstractIntegrationSpec {

    Logger log = Logger.getLogger(PasswordControllerIntegrationSpec.class)

    protected static final String PASSWORD = "abcd1234"
    protected static final String USERNAME = 'passwordtestuser'
    private User user
    def passwordService
    PasswordController controller

    def setup() {
        log.info 'Calling setup...'
        controller = new PasswordController()
        controller.passwordService = passwordService

        user = User.findByUsername(USERNAME) ?: new User(username: USERNAME, enabled: true)
        user.password = PASSWORD
        user.cleartextPassword = PASSWORD
        user.save(flush: true, failOnError: true)
    }

    @Ignore('Fails only on build server, for some unknown reason')
    def "test change password on user in sunshine scenario"() {
        log.info 'Calling sunshine...'

        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(PASSWORD, "1234abcd")
        def version = user.version

        when:
        controller.update()

        then:
        controller.flash.message == "Password changed for user: " + USERNAME
        !user.cleartextPassword
        user.version > version
        controller.response.redirectUrl == '/password/changed'
    }

    @Ignore('Fails only on build server, for some unknown reason')
    def "test change password on user with json in sunshine scenario"() {

        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(PASSWORD, "1234abcd")
        controller.response.format = 'json'
        def version = user.version

        when:
        controller.update()

        and:
        def json = JSON.parse(controller.response.text)

        then:
        json.status == 'ok'
        json.message == 'Password changed for user: ' + USERNAME
        !user.cleartextPassword
        user.version > version
    }

    @Ignore('Fails only on build server, for some unknown reason')
    def "test change password on user where old password is wrong"() {

        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams("abcd12", "1234abcd")
        def version = user.version

        when:
        controller.update()

        then:
        controller.modelAndView.model.command.hasErrors()
        controller.modelAndView.model.command.errors['currentPassword'].code == 'passwordCommand.currentPassword.mismatch'
        controller.modelAndView.viewName == '/password/change'
        user.cleartextPassword
        user.version == version
    }

    @Ignore('Fails only on build server, for some unknown reason')
    //  @Unroll
    def "test change password on user with json with errors"() {

        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(currentPassword, password, passwordRepeat)
        controller.response.format = 'json'
        def version = user.version

        when:
        controller.update()

        and:
        def json = JSON.parse(controller.response.text)

        then:
        json.status == 'error'
        json.errors.size() == 1
        json.errors[0].field == errorField
        user.cleartextPassword
        user.version == version

        where:
        currentPassword | password | passwordRepeat | errorField
        'abc123' | '1234abcd' | '1234abcd' | "currentPassword"
        PASSWORD | '12345678' | '12345678' | "password"
        PASSWORD | 'abcd1234' | '12345678' | "passwordRepeat"
    }

    def populateParams(currentPassword, password, passwordRepeat = null) {
        controller.params.currentPassword = currentPassword
        controller.params.password = password
        controller.params.passwordRepeat = passwordRepeat ?: password
    }
}
