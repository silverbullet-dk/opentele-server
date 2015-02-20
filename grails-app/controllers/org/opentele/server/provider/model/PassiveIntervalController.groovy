package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.PassiveInterval
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class PassiveIntervalController {

    def sessionService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.PASSIVE_INTERVAL_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        if (!params.sort)
            params.sort = 'intervalStartDate'

        if (!params.order)
            params.order = 'asc'

        Patient patient = Patient.get(params.id)
        sessionService.setPatient(session, patient)

        def intervals = PassiveInterval.findAllByPatient(patient, [sort: params.sort, order: params.order, max: params.max, offset: params.offset])
        [passiveIntervalInstanceList: intervals, passiveIntervalInstanceTotal: PassiveInterval.countByPatient(patient), patient: patient]
    }
    @Secured(PermissionName.PASSIVE_INTERVAL_CREATE)
    def create() {
        [passiveIntervalInstance: new PassiveInterval(params)]
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_CREATE)
    def save() {
        params.patient = Patient.get(session[Constants.SESSION_PATIENT_ID])

        def passiveIntervalInstance = new PassiveInterval(params)

        if (!passiveIntervalInstance.save(flush: true)) {
            render(view: "create", model: [passiveIntervalInstance: passiveIntervalInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'passiveInterval.label'), passiveIntervalInstance.id])
        redirect(action: "show", id: passiveIntervalInstance.id)
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_READ)
    def show() {
        def passiveIntervalInstance = PassiveInterval.get(params.id)
        if (!passiveIntervalInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "list", id: session[Constants.SESSION_PATIENT_ID])
            return
        }

        [passiveIntervalInstance: passiveIntervalInstance]
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_WRITE)
    def edit() {
        def passiveIntervalInstance = PassiveInterval.get(params.id)
        if (!passiveIntervalInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "list", id: session[Constants.SESSION_PATIENT_ID])
            return
        }

        [passiveIntervalInstance: passiveIntervalInstance]
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_WRITE)
    def update() {
        def passiveIntervalInstance = PassiveInterval.get(params.id)

        sessionService?.setPatient(session, passiveIntervalInstance.patient)

        if (!passiveIntervalInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "list", id: session[Constants.SESSION_PATIENT_ID])
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (passiveIntervalInstance.version > version) {
                passiveIntervalInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'passiveInterval.label', default: 'PassiveInterval')] as Object[],
                        "Another user has updated this PassiveInterval while you were editing")
                render(view: "edit", model: [passiveIntervalInstance: passiveIntervalInstance])
                return
            }
        }

        passiveIntervalInstance.properties = params

        if (!passiveIntervalInstance.save(flush: true)) {
            render(view: "edit", model: [passiveIntervalInstance: passiveIntervalInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'passiveInterval.label'), passiveIntervalInstance.id])
        redirect(action: "show", id: passiveIntervalInstance.id)
    }

    @Secured(PermissionName.PASSIVE_INTERVAL_DELETE)
    def delete() {

        def passiveIntervalInstance = PassiveInterval.get(params.id)

        sessionService?.setPatient(session, passiveIntervalInstance.patient)

        passiveIntervalInstance.patient.removeFromPassiveIntervals(passiveIntervalInstance)

        if (!passiveIntervalInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "list", id: session[Constants.SESSION_PATIENT_ID])
            return
        }

        try {
            passiveIntervalInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "list", id: session[Constants.SESSION_PATIENT_ID])
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'passiveInterval.label'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
