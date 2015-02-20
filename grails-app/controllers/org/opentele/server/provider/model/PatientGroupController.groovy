package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.PatientGroup
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class PatientGroupController {

    def patientGroupService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.PATIENT_GROUP_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.PATIENT_GROUP_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [patientGroupInstanceList: PatientGroup.list(params), patientGroupInstanceTotal: PatientGroup.count()]
    }

    @Secured(PermissionName.PATIENT_GROUP_CREATE)
    def create() {
        [patientGroupInstance: new PatientGroup(params)]
    }

    @Secured(PermissionName.PATIENT_GROUP_CREATE)
    def save() {

        params.name = params.name.encodeAsHTML()

        def patientGroupInstance = new PatientGroup(params)
        StandardThresholdSet sts = new StandardThresholdSet()
        sts.save(failOnError: true, flush: true)
        patientGroupInstance.setStandardThresholdSet(sts)
        if (!patientGroupInstance.save(flush: true)) {
            sts.delete()
            render(view: "create", model: [patientGroupInstance: patientGroupInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), patientGroupInstance.id])
        redirect(action: "show", id: patientGroupInstance.id)
    }

    @Secured(PermissionName.PATIENT_GROUP_READ)
    def show() {
        def patientGroupInstance = PatientGroup.get(params.id)
        if (!patientGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "list")
            return
        }

        [patientGroupInstance: patientGroupInstance]
    }

    @Secured(PermissionName.PATIENT_GROUP_WRITE)
    def edit() {
        def patientGroupInstance = PatientGroup.get(params.id)
        if (!patientGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "list")
            return
        }

        [patientGroupInstance: patientGroupInstance]
    }

    @Secured(PermissionName.PATIENT_GROUP_WRITE)
    def update() {
        def patientGroupInstance = PatientGroup.get(params.id)
        if (!patientGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (patientGroupInstance.version > version) {
                patientGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'patientGroup.label', default: 'PatientGroup')] as Object[],
                        "Another user has updated this PatientGroup while you were editing")
                render(view: "edit", model: [patientGroupInstance: patientGroupInstance])
                return
            }
        }

        patientGroupInstance.properties = params

        if (!patientGroupInstance.save(flush: true)) {
            render(view: "edit", model: [patientGroupInstance: patientGroupInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), patientGroupInstance.id])
        redirect(action: "show", id: patientGroupInstance.id)
    }

    @Secured(PermissionName.PATIENT_GROUP_DELETE)
    def delete() {
        def patientGroupInstance = PatientGroup.get(params.id)
        if (!patientGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "list")
            return
        }

        if (patientGroupService.deletePatientGroup(patientGroupInstance)) {
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "list")
        } else {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'patientGroup.label', default: 'PatientGroup'), params.id])
            redirect(action: "show", id: params.id)

        }
    }
}
