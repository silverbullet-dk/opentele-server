package org.opentele.taglib

class MailSenderTagLib {
    static namespace = "mailsender"
    def mailSenderService


    def isEnabled = { attrs, body ->
        if(mailSenderService.mailEnabled && body instanceof Closure) {
            out << body()
        }
    }
}
