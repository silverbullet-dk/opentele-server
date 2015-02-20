package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.NextOfKinPerson
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class NextOfKinPersonController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def patientService
    def sessionService
    @Secured(PermissionName.NEXT_OF_KIN_CREATE)
    def create() {
        def patientInstance = Patient.findById(params.patientId)
        if (!patientInstance) {
            redirect(controller:"patient", action:"overview")
            return
        }
        sessionService.setPatient(session, patientInstance)
        [nextOfKinPersonInstance: new NextOfKinPerson(params), patientInstanceId: patientInstance.id]
    }

    @Secured(PermissionName.NEXT_OF_KIN_CREATE)
    def save() {
        def nextOfKinPersonInstance = new NextOfKinPerson(params)
        def patientInstance = Patient.findById(params.patientID)

        if (!patientInstance) {
            nextOfKinPersonInstance.errors.reject(message(code: 'nextOfKinPersonInstance.create.patient.not.found'))
            render(view: "create", model: [nextOfKinPersonInstance: nextOfKinPersonInstance, patientInstanceId: params.patientID])
            return
        }

        nextOfKinPersonInstance.patient = patientInstance
        if (!nextOfKinPersonInstance.save(flush: true)) {
            render(view: "create", model: [nextOfKinPersonInstance: nextOfKinPersonInstance, patientInstanceId: params.patientID])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'patient2NextOfKinPerson.label', default: 'NextOfKinPerson'), nextOfKinPersonInstance.firstName+" "+nextOfKinPersonInstance.lastName])

        patientInstance.nextOfKinPersons.add(nextOfKinPersonInstance)
        patientInstance.save()

        //Redirect back to edit view of patient -- where we left
        redirect(controller: "patient", action: "edit", params: [id: patientInstance.id])
    }

    @Secured(PermissionName.NEXT_OF_KIN_READ)
    def show() {
        def nextOfKinPersonInstance = NextOfKinPerson.get(params.id)
        if (!nextOfKinPersonInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nextofkin.label')])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(controller: "patient", action: "edit", id: patient.id)
            return
        }

        sessionService.setPatient(session, nextOfKinPersonInstance.patient)
        [nextOfKinPersonInstance: nextOfKinPersonInstance]
    }

    @Secured(PermissionName.NEXT_OF_KIN_WRITE)
    def edit() {
        def nextOfKinPersonInstance = NextOfKinPerson.get(params.id)
        if (!nextOfKinPersonInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nextofkin.label')])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(controller: "patient", action: "edit", id: patient.id)
            return
        }

        sessionService.setPatient(session, nextOfKinPersonInstance.patient)
        [nextOfKinPersonInstance: nextOfKinPersonInstance]
    }

    @Secured(PermissionName.NEXT_OF_KIN_WRITE)
    def update() {
        def nextOfKinPersonInstance = NextOfKinPerson.get(params.id)
        if (!nextOfKinPersonInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nextofkin.label')])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(controller: "patient", action: "edit", id: patient.id)
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (nextOfKinPersonInstance.version > version) {
                nextOfKinPersonInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'nextofkin.label')] as Object[],
                        "Another user has updated this NextOfKinPerson while you were editing")
                render(view: "edit", model: [nextOfKinPersonInstance: nextOfKinPersonInstance])
                return
            }
        }

        nextOfKinPersonInstance.properties = params

        if (!nextOfKinPersonInstance.save(flush: true)) {
            render(view: "edit", model: [nextOfKinPersonInstance: nextOfKinPersonInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'nextofkin.label')])
        redirect(action: "show", id: nextOfKinPersonInstance.id)
    }

    @Secured(PermissionName.NEXT_OF_KIN_DELETE)
    def delete() {
        def nextOfKinPersonInstance = NextOfKinPerson.get(params.id)
        if (!nextOfKinPersonInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nextofkin.label')])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(controller: "patient", action: "edit", id: patient.id)
            return
        }

        try {
            nextOfKinPersonInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'nextofkin.label')])
            redirect(controller: "patient", action: "edit", id: session[Constants.SESSION_PATIENT_ID])
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nextofkin.label')])
            redirect(action: "show", id: params.id)
        }
    }
}
