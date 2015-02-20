package org.opentele.server

import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.core.LoginController
import org.opentele.server.core.PasswordService
import org.opentele.server.model.User
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.web.WebAttributes
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(LoginController)
@Build([User])
class LoginControllerSpec extends Specification {
    def setup() {
        User.metaClass.encodePassword = {-> }

        controller.passwordService = Mock(PasswordService)
        controller.springSecurityService = Mock(SpringSecurityService)
    }

    @Unroll
    def "when login fails due to password failed attempts, the message returns depends on number of attempts"() {
        setup:
        session[WebAttributes.AUTHENTICATION_EXCEPTION] = new BadCredentialsException("")
        flash.badloginAttempt  = passwordReset
        when:
        controller.authfail()

        then:
        flash.message == message

        where:
        passwordReset | message
        false    | 'springSecurity.errors.login.fail'
        true     | 'patient.login.fail.password.reset'
    }

}
