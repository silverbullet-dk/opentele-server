package org.opentele.server.core
import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.util.Environment

import org.opentele.server.model.User

@SkipAuditLog
@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class MetaController {
    def sessionRegistry
    def index() {}
    def currentServerVersion() {
        def version = grailsApplication.metadata['app.version']
        render(contentType: 'text/json') {[
                'version': version,
                'serverEnvironment': Environment.getCurrent()?.getName()
        ]}
    }

    def isAlive() {
        def info = buildIsAliveInformation()
        render(view: "/meta", model: [info: info])
    }

    def isAliveJSON() {
        def info = buildIsAliveInformation()
        render text: "jsonCallback(${info as JSON});", contentType: 'text/javascript'
    }

    def registerCurrentPage(String currentController, String currentAction) {
        if(!request.xhr) {
            redirect(action: 'noAccess')
        } else {
            params.remove('action')
            params.remove('controller')
            session.currentController = currentController
            session.currentAction = currentAction
            session.currentParams = params
            log.debug "Current page is: /${session.currentController}/${session.currentAction}"
            render(contentType:  "text/json") {
                [status: "ok"]
            }
        }
    }

    def noAccess() {
    }

    private Map<String, String> buildIsAliveInformation() {
        def info = [:]
        info['responseText'] = "I'm alive!"
        info['runningVersion'] = grailsApplication.metadata['app.version']
        info['environment'] = Environment.getCurrent()?.getName()

        def dbAlive
        try {
             User.executeQuery("select 1 from User").size() > 0
             dbAlive = 'Yes.'
        } catch (Exception e) {
            dbAlive = 'No. '+e.getMessage()
        }

        info['isDatabaseAlive'] = dbAlive
        info['concurrentUsers'] = getNumberOfActiveUsers()
        return info
    }

    private def getNumberOfActiveUsers() {
        def cnt = 0

        sessionRegistry.getAllPrincipals().each{
            cnt += sessionRegistry.getAllSessions(it, false).size()
        }
        return cnt
    }
}
