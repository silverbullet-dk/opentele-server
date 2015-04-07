package org.opentele.server.provider.model
import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.core.command.CreateClinicianCommand
import org.opentele.server.model.Clinician
import org.opentele.server.model.Clinician2PatientGroup
import org.opentele.server.core.command.ClinicianCommand
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class ClinicianController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def clinicianService
    def passwordService
    def springSecurityService
    def authenticationManager

    @Secured(PermissionName.CLINICIAN_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.CLINICIAN_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        log.debug(params)
        if (params.sort.equals("username")) {//Non-boilerplate column included for this DOM
            params.sort = null //'username' is a property of Clinician.User, not Clinician hence list(..) can use that for sorting
            def clinicians = Clinician.list(params).sort{a, b -> params.order.equals("asc") ? a.user.username <=> b.user.username : b.user.username <=> a.user.username}
            params.sort = "username" //For automatic 'asc' / 'desc' change
            return [clinicianInstanceList: clinicians, clinicianInstanceTotal: Clinician.count()]
        } else {
            return [clinicianInstanceList: Clinician.list(params), clinicianInstanceTotal: Clinician.count()]
        }

    }

    @Secured(PermissionName.CLINICIAN_CREATE)
    def create() {
        if (!params.cleartextPassword) {
            params.cleartextPassword = passwordService.generateTempPassword()
        }
        def command = new CreateClinicianCommand()
        bindData(command, params, ["action", "controller"])

        [cmd: command]
    }

    @Secured(PermissionName.CLINICIAN_CREATE)
    def save(CreateClinicianCommand command) {
        if (!command.validate()) {
            render(view: "create", model: [cmd: command])
            return;
        }

        def clinicianInstance = clinicianService.createClinician(command)
        if (!clinicianInstance.hasErrors()) {
            flash.message = message(code: 'default.created.message', args: [message(code: 'clinician.label'), clinicianInstance.firstName])
            redirect(action: "show", id: clinicianInstance.id)
        } else {
            render(view: "create", model: [cmd: command])
        }
    }

    @Secured(PermissionName.CLINICIAN_READ)
    def show(Long id) {
        def clinicianInstance = Clinician.get(id)
        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }

        [clinicianInstance: clinicianInstance, isCurrentUser: springSecurityService.currentUser == clinicianInstance.user]
    }

    @Secured(PermissionName.CLINICIAN_WRITE)
    def edit(Long id) {
        def command = clinicianService.getClinicianCommandForEdit(id)
        if (!command) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }
        [cmd: command]
    }

    @Secured(PermissionName.CLINICIAN_WRITE)
    def update() {
        def clinicianInstance = Clinician.get(params.id)
        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }

        def command = new ClinicianCommand()
        bindData(command, params)
        command.validate()

        clinicianInstance = clinicianService.update(command, clinicianInstance)
        if (command.hasErrors() || clinicianInstance.hasErrors()) {
            render(view: "edit", model: [cmd: command])
        } else {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "show", id: clinicianInstance.id)
        }
    }

    @Secured(PermissionName.CLINICIAN_DELETE)
    def delete(Long id) {
        def clinicianInstance = Clinician.get(id)
        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }

        try {
            clinicianService.delete(clinicianInstance)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "show", id: id)
        }
    }


    @Secured(PermissionName.CLINICIAN_PATIENT_GROUP_DELETE)
    def removePatientGroup() {
        def clinicianInstance = Clinician.get(params.clinician.id)
        def clinicianPatientGroups = Clinician2PatientGroup.findAllByClinician(clinicianInstance)

        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }
        [clinicianInstance: clinicianInstance, clinicianPatientGroups: clinicianPatientGroups]
    }

    @Secured(PermissionName.CLINICIAN_PATIENT_GROUP_DELETE)
    def doRemovePatientGroup() {
        def clinicianInstance = Clinician.get(params.clinician.id)
        def clinician2PatientGroup = Clinician2PatientGroup.get(params.patientGroup.id)
        def patientGroupInstance = clinician2PatientGroup.getPatientGroup()

        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }

        if (!patientGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician2PatientGroup.label', default: 'Patient Group')])
            redirect(action: "list")
            return
        }

        Clinician2PatientGroup.unlink(clinicianInstance, patientGroupInstance);

        redirect(action: "show", id: clinicianInstance.id)
    }

    @Secured(PermissionName.CLINICIAN_CHANGE_PASSWORD)
    def resetPassword(Long id) {
        def clinicianInstance = Clinician.get(id)
        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }

        clinicianService.resetPassword(clinicianInstance)
        flash.message = message(code: 'clinician.reset-password.done', args: [clinicianInstance.user.username])
        redirect(action: "show", id: id)

    }

    @Secured(PermissionName.CLINICIAN_CHANGE_PASSWORD)
    def unlockAccount(Long id) {
        def clinicianInstance = Clinician.get(id)
        if (!clinicianInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinician.label', default: 'Clinician')])
            redirect(action: "list")
            return
        }
        passwordService.unlockAccount(clinicianInstance.user)
        flash.message = message(code: 'clinician.unlock-account.done', args: [clinicianInstance.user.username])
        redirect(action: "show", id: id)
    }

}


