package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.provider.ThresholdService

import org.opentele.server.model.NumericThreshold
import org.opentele.server.core.command.NumericThresholdCommand
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.provider.StandardThresholdSetService

@Secured(PermissionName.NONE)
class NumericThresholdController  {
    ThresholdService thresholdService
    StandardThresholdSetService standardThresholdSetService
    static allowedMethods = [update: "POST"]

    @Secured(PermissionName.THRESHOLD_WRITE)
    def edit(Long id) {
        session.setAttribute('lastReferer', request.getHeader('referer'))
        def command = thresholdService.getThresholdCommandForEdit(NumericThreshold.get(id))
        def threshold = command?.threshold
        if (!threshold) {
            return notFound(id)
        }

        def standardThresholdSet = standardThresholdSetService.findStandardThresholdSetForThreshold(threshold)

        render(view: '/threshold/edit', model: [
                command: command,
                patientGroup: standardThresholdSet?.patientGroup
        ])
    }

    @Secured(PermissionName.THRESHOLD_WRITE)
    def update(NumericThresholdCommand command) {
        thresholdService.updateThreshold(command)
        if(command.hasErrors()) {
            def threshold = command?.threshold
            if(!threshold) {
                return notFound(params.id)
            }
            def standardThresholdSet = standardThresholdSetService.findStandardThresholdSetForThreshold(threshold)
            render(view: '/threshold/edit', model: [
                    command: command,
                    patientGroup: standardThresholdSet?.patientGroup
            ])
        } else {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), command.threshold.id])
            def lastReferer = session.getAttribute('lastReferer')
            if (lastReferer) {
                session.removeAttribute('lastReferer')
                redirect(url: lastReferer)
            }
        }
    }

    private notFound(def id) {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), id])
        redirect(action: session.lastAction, controller: session.lastController)
    }


}
