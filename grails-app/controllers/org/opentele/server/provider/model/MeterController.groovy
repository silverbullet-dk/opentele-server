package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Meter
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class MeterController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.METER_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.METER_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [meterInstanceList: Meter.list(params), meterInstanceTotal: Meter.count()]
    }

    @Secured(PermissionName.METER_CREATE)
    def create() {
        [meterInstance: new Meter(params)]
    }

    @Secured(PermissionName.METER_CREATE)
    def save() {
        def meterInstance = new Meter(params)
        if (!meterInstance.save(flush: true)) {
            render(view: "create", model: [meterInstance: meterInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'meter.label', default: 'Meter')])
        redirect(action: "show", id: meterInstance.id)
    }

    @Secured(PermissionName.METER_READ)
    def show() {
        def meterInstance = Meter.get(params.id)
        if (!meterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "list")
            return
        }

        [meterInstance: meterInstance]
    }

    @Secured(PermissionName.METER_WRITE)
    def edit() {
        def meterInstance = Meter.get(params.id)
        if (!meterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "list")
            return
        }

        [meterInstance: meterInstance]
    }

    @Secured(PermissionName.METER_READ)
    def update() {
        def meterInstance = Meter.get(params.id)
        if (!meterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (meterInstance.version > version) {
                meterInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'meter.label', default: 'Meter')] as Object[],
                          "Another user has updated this Meter while you were editing")
                render(view: "edit", model: [meterInstance: meterInstance])
                return
            }
        }

        meterInstance.properties = params

        if (!meterInstance.save(flush: true)) {
            render(view: "edit", model: [meterInstance: meterInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'meter.label', default: 'Meter')])
        redirect(action: "show", id: meterInstance.id)
    }

    @Secured(PermissionName.METER_DELETE)
    def delete() {
        def meterInstance = Meter.get(params.id)
        if (!meterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "list")
            return
        }

        try {
            meterInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'meter.label', default: 'Meter')])
            redirect(action: "show", id: params.id)
        }
    }
}
