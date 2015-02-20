package org.opentele.server.provider.service

import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MailSenderService)
class MailSenderServiceSpec extends Specification {
    @Shared sendMailVerifier = new SendMailVerifier()
    def setup() {
        service.grailsApplication = Mock(GrailsApplication)
        service.mailService = Mock(MailService)
        service.groovyPageRenderer = Mock(PageRenderer)
    }

    @Unroll
    def "test mailAvailable property dependent on the configuration in Config"() {
        given:
        service.grailsApplication.config >> [grails: [mail: [disabled: disabled, host: host]]]

        expect:
        service.mailEnabled == enabled

        where:
        disabled | host        | enabled
        false    | "some.host" | true
        false    | ""          | false
        true     | "some.host" | false
    }

    def "test sendMail with params and templates"() {
        when:
        def result = service.sendMail("subject", "to@example.com", "template", [model: 1])

        then:
        result
        1 * service.groovyPageRenderer.render([template: "template",model: [model: 1]]) >> "rendered template"
        1 * service.mailService.sendMail({
            it.delegate = sendMailVerifier
            it.call()
            true
        })
        !sendMailVerifier.from
        sendMailVerifier.to == "to@example.com"
        sendMailVerifier.subject == "subject"
        sendMailVerifier.html == "rendered template"
    }

    static class SendMailVerifier {
        String from
        String to
        String subject
        String html

        def from(String from) {
            this.from = from
        }
        def to(String to) {
            this.to = to
        }
        def subject(String subject) {
            this.subject = subject
        }
        def html(String html) {
            this.html = html
        }
    }
}
