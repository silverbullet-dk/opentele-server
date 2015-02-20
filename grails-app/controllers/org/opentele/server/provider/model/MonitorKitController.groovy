package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Meter
import org.opentele.server.model.MonitorKit
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class MonitorKitController {
	static allowedMethods = [save: "POST", update: "POST", delete: "POST", attachMeter:["GET","POST"]]

    @Secured(PermissionName.MONITOR_KIT_READ_ALL)
	def index() {
		redirect(action: "list", params: params)
	}

    @Secured(PermissionName.MONITOR_KIT_READ_ALL)
	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[monitorKitInstanceList: MonitorKit.list(params), monitorKitInstanceTotal: MonitorKit.count()]
	}

    @Secured(PermissionName.MONITOR_KIT_CREATE)
	def create() {
		[monitorKitInstance: new MonitorKit(params)]
	}

    @Secured(PermissionName.MONITOR_KIT_WRITE)
	def removeMeter() {
		def kit = MonitorKit.get(params.id)
		def meters = Meter.findAllByMonitorKit(kit)

		if (request.method == "POST") {
			def meter = Meter.get(params.meter.id)

			kit.removeFromMeters(meter)
			if (!kit.save()) {
				render (view:"removeMeter", model: [monitorKitInstance:kit, meters:meters])
				return
			} else {
				redirect (action:"show", id: kit.id)
				return
			}
		} else {
			[monitorKitInstance:kit, meters:meters]
		}
	}

    @Secured(PermissionName.MONITOR_KIT_WRITE)
	def attachMeter() {
		def meters = Meter.findAllByActiveAndMonitorKit(true,null)
		def kit = MonitorKit.get(params.id)

		if (request.method == "POST") {
			def meter = Meter.get(params.meter.id)

			kit.addToMeters(meter)
            kit.save()

            if (kit.patient) {
                meter.patient = kit.patient
                meter.save()
            }

			if (kit.hasErrors()||meter.hasErrors()) {
				render (view:"attachMeter", model: [monitorKitInstance:kit, meters:meters])
				return
			} else {
				redirect (action:"show", id: kit.id)
				return
			}
		} else {
			[monitorKitInstance:kit, meters:meters]
		}
	}

    @Secured(PermissionName.MONITOR_KIT_CREATE)
	def save() {
		def monitorKitInstance = new MonitorKit(params)

		if (!monitorKitInstance.save(flush: true)) {
			render(view: "create", model: [monitorKitInstance: monitorKitInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
		redirect(action: "show", id: monitorKitInstance.id)
	}

    @Secured(PermissionName.MONITOR_KIT_READ)
	def show() {
		def monitorKitInstance = MonitorKit.get(params.id)
		if (!monitorKitInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "list")
			return
		}

		[monitorKitInstance: monitorKitInstance]
	}

    @Secured(PermissionName.MONITOR_KIT_WRITE)
	def edit() {
		def monitorKitInstance = MonitorKit.get(params.id)
		if (!monitorKitInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "list")
			return
		}

		[monitorKitInstance: monitorKitInstance]
	}

    @Secured(PermissionName.MONITOR_KIT_WRITE)
	def update() {
		def monitorKitInstance = MonitorKit.get(params.id)
		if (!monitorKitInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (monitorKitInstance.version > version) {
				monitorKitInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'monitorKit.label', default: 'MonitorKit')] as Object[],
						"Another user has updated this MonitorKit while you were editing")
				render(view: "edit", model: [monitorKitInstance: monitorKitInstance])
				return
			}
		}

		monitorKitInstance.properties = params

		if (!monitorKitInstance.save(flush: true)) {
			render(view: "edit", model: [monitorKitInstance: monitorKitInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
		redirect(action: "show", id: monitorKitInstance.id)
	}

    @Secured(PermissionName.MONITOR_KIT_DELETE)
	def delete() {
		def monitorKitInstance = MonitorKit.get(params.id)
		if (!monitorKitInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "list")
			return
		}

		try {
			monitorKitInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'monitorKit.label', default: 'MonitorKit')])
			redirect(action: "show", id: params.id)
		}
	}
}
