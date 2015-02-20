package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.ScheduleWindow
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class ScheduleWindowController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.SCHEDULEWINDOW_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.SCHEDULEWINDOW_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [scheduleWindowInstanceList: ScheduleWindow.list(params), scheduleWindowInstanceTotal: ScheduleWindow.count()]
    }

    @Secured(PermissionName.SCHEDULEWINDOW_READ)
    def show() {
        def scheduleWindowInstance = ScheduleWindow.get(params.id)
        if (!scheduleWindowInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'scheduleWindow.label', default: 'ScheduleWindow'), params.id])
            redirect(action: "list")
            return
        }

        [scheduleWindowInstance: scheduleWindowInstance]
    }

    @Secured(PermissionName.SCHEDULEWINDOW_WRITE)
    def edit() {
        def scheduleWindowInstance = ScheduleWindow.get(params.id)
        if (!scheduleWindowInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'scheduleWindow.label', default: 'ScheduleWindow'), params.id])
            redirect(action: "list")
            return
        }

        [scheduleWindowInstance: scheduleWindowInstance]
    }
    @Secured(PermissionName.SCHEDULEWINDOW_WRITE)
    def update() {
        def scheduleWindowInstance = ScheduleWindow.get(params.id)
        if (!scheduleWindowInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'scheduleWindow.label', default: 'ScheduleWindow'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (scheduleWindowInstance.version > version) {
                scheduleWindowInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'scheduleWindow.label', default: 'ScheduleWindow')] as Object[],
                        "Another user has updated this ScheduleWindow while you were editing")
                render(view: "edit", model: [scheduleWindowInstance: scheduleWindowInstance])
                return
            }
        }

        scheduleWindowInstance.properties = params

        if (!scheduleWindowInstance.save(flush: true)) {
            render(view: "edit", model: [scheduleWindowInstance: scheduleWindowInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'scheduleWindow.label', default: 'ScheduleWindow'), scheduleWindowInstance.id])
        redirect(action: "show", id: scheduleWindowInstance.id)
    }
}
