package org.opentele.server.provider.service

import grails.gsp.PageRenderer
import grails.plugin.mail.MailService

class MailSenderService {

    def grailsApplication
    MailService mailService
    PageRenderer groovyPageRenderer

    boolean getMailEnabled() {
        !grailsApplication.config.grails.mail.disabled && grailsApplication.config.grails.mail.host
    }

    boolean sendMail(String mailSubject, String mailTo, String template, Map model) {
        def mailHtml = renderTemplate(template, model)
        mailService.sendMail {
            to(mailTo)
            subject(mailSubject)
            html(mailHtml)
        }
        return true
    }

    private String renderTemplate(String template, Map model) {
        groovyPageRenderer.render (template: template, model: model)

    }
}
