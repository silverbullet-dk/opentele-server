package org.opentele.taglib

import grails.test.mixin.TestFor
import org.opentele.server.provider.service.MailSenderService
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(MailSenderTagLib)
class MailSenderTagLibSpec extends Specification {

    def setup() {
        tagLib.mailSenderService = Mock(MailSenderService)

    }

    def "test that when mail is enabled, output will be rendered"() {
        given:
        tagLib.mailSenderService.mailEnabled >> true

        expect:
        applyTemplate("<mailsender:isEnabled>output</mailsender:isEnabled>") == "output"
    }
    def "test that when mail is disabled, output will not be rendered"() {
        given:
        tagLib.mailSenderService.mailEnabled >> false

        expect:
        !applyTemplate("<mailsender:isEnabled>output</mailsender:isEnabled>")
    }

}
