package org.opentele.server.provider

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.opentele.server.core.command.ClinicianCommand
import org.opentele.server.core.command.CreateClinicianCommand
import org.opentele.server.core.exception.ClinicianException
import org.opentele.server.model.*
import org.springframework.transaction.annotation.Transactional

@Transactional
class ClinicianService {
    def springSecurityService
    def passwordService

    // From here: http://nerderg.com/Grails - makes the bindData available for services too
    ClinicianService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    /**
     * Creates a patient and associated user. Throws a RuntimeException, which rolls the entire operation back in case of error.
     */
    Clinician createClinician(CreateClinicianCommand command) {
        def clinicianInstance = new Clinician(command.properties)
        clinicianInstance.user = new User(command.properties)
        clinicianInstance.user.enabled = true

        if (!clinicianInstance.user.validate()) {
            clinicianInstance.user.errors.fieldErrors.each {
                clinicianInstance.errors.rejectValue("user.${it.field}", it.code, it.arguments, it.defaultMessage)
            }
        }

        if (clinicianInstance.user.save(flush: true) && clinicianInstance.save(flush: true)) {
            updateRoleLinks(command.roleIds, clinicianInstance)
            updatePatientGroupLinks(command.groupIds, clinicianInstance)
        }

        return clinicianInstance
    }

    def updatePatientGroupLinks(List<Long> groupIds, Clinician clinician) {
        def oldPatientGroups = Clinician2PatientGroup.findAllByClinician(clinician)*.patientGroup

        def newPatientGroups = PatientGroup.findAllByIdInList(groupIds)

        newPatientGroups.each { patientGroup ->
            Clinician2PatientGroup.link(clinician, patientGroup)
        }

        def obsoletePatientGroups = oldPatientGroups - newPatientGroups

        obsoletePatientGroups.each {
            Clinician2PatientGroup.unlink(clinician, it)
        }
    }

    def updateRoleLinks(List<Long> roleIds, Clinician clinician) {
        UserRole.removeAll(clinician.user, true)
        Role.findAllByIdInList(roleIds).each { role ->
            def userRole = new UserRole(user: clinician.user, role: role)
            if (!userRole.save()) {
                throw new ClinicianException("clinician.could.not.assign.clinician.rights", userRole.errors)
            }
        }
    }

    def resetPassword(Clinician clinician) {
        passwordService.resetPassword(clinician.user)
    }

    def update(ClinicianCommand command, Clinician clinicianInstance) {
        if (clinicianInstance.version > command.version) {
            clinicianInstance.errors.rejectValue("version", "clinician.optimistic.locking.failure",
                    "Another user has updated this Clinician while you were editing")
            return
        }
        if (command.cleartextPassword && command.validate(['cleartextPassword']) && command.cleartextPassword != clinicianInstance.user.cleartextPassword) {
            clinicianInstance.user.cleartextPassword = command.cleartextPassword
            clinicianInstance.user.password = command.password
            if (!clinicianInstance.user.save()) {
                return clinicianInstance
            }
        }
        if (!command.hasErrors() && command.validate(['firstName', 'lastName', 'phone', 'mobilePhone', 'email', 'video_user', 'video_password', 'groupIds', 'roleIds'])) {
            clinicianInstance.properties = command.properties
            if (clinicianInstance.save()) {
                updateRoleLinks(command.roleIds, clinicianInstance)
                updatePatientGroupLinks(command.groupIds, clinicianInstance)
            }
            clinicianInstance.save(flush: true)
        }
        return clinicianInstance
    }

    def delete(Clinician clinicianInstance) {
        def patientGroups = Clinician2PatientGroup.findAllByClinician(clinicianInstance)*.patientGroup
        patientGroups.each {
            Clinician2PatientGroup.unlink(clinicianInstance, it)
        }

        clinicianInstance.user.enabled = false
        clinicianInstance.delete(flush: true)
        // TODO: Should it also delete the user?? or at least disable it?
    }

    def ClinicianCommand getClinicianCommandForEdit(long id) {
        def clinicianInstance = Clinician.get(id)
        if (clinicianInstance) {
            def command = new ClinicianCommand(id: clinicianInstance.id, version: clinicianInstance.version)
            bindData(command, clinicianInstance.properties)
            bindData(command, clinicianInstance.user.properties)
            command.roleIds = UserRole.findAllByUser(clinicianInstance.user) collect { it.roleId }
            command.groupIds = Clinician2PatientGroup.findAllByClinician(clinicianInstance).collect {
                it.patientGroupId
            }
            return command
        }
        return null
    }

    def getCurrentClinician() {
        def user = (User) springSecurityService.currentUser
        return Clinician.findByUser(user)
    }

    List<PatientGroup> getPatientGroupsForCurrentClinician() {
        Clinician2PatientGroup.findAllByClinician(currentClinician).collect { it.patientGroup }.sort { it.name }
    }

    void saveUserPreference(Clinician clinician, String preference, Object value) {
        UserPreference userPreference = UserPreference.findByClinicianAndPreference(clinician, preference)
        if (!userPreference) {
            userPreference = new UserPreference(clinician: clinician, preference: preference)
        }

        userPreference.value = value.toString()
        userPreference.save()
    }

    String getUserPreference(Clinician clinician, String preference) {
        UserPreference userPreference = UserPreference.findByClinicianAndPreference(clinician, preference)
        log.debug 'Got userPreference..:' + userPreference
        return userPreference?.value
    }
}
