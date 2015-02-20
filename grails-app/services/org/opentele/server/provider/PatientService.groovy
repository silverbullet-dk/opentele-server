package org.opentele.server.provider

import grails.plugin.springsecurity.SpringSecurityUtils
import org.opentele.server.provider.constants.Constants
import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.core.command.PatientSearchCommand
import org.opentele.server.core.exception.*
import org.opentele.server.model.*
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.PermissionName
import org.springframework.transaction.annotation.Transactional

@Transactional
class PatientService {
    def springSecurityService
    def passwordService
    def mailSenderService
    def i18nService
    def clinicianService
    def patientOverviewMaintenanceService

    @Transactional
    def buildAndSavePatient(CreatePatientCommand cmd) {

        //Everything is validated in the CreatePatientCommand object

        //Build User object first
        Role patientRole = Role.findByAuthority(Constants.DEFAULT_PATIENT_ROLE)
        User patientUser = new User(username: cmd.username,
                password: cmd.cleartextPassword,
                cleartextPassword: cmd.cleartextPassword,
                accountExpired: false,
                accountLocked: false,
                enabled: true,
                passwordExpired: false
        )
        if (!patientUser.save()) {
            throw new PatientException("patient.could.not.create.user", patientUser.errors)
        }

        UserRole userRole = new UserRole(user: patientUser, role: patientRole)
        if (!userRole.save()) {
            throw new PatientException("patient.could.not.assign.patient.rights", userRole.errors)
        }

        //Then build Patient object
        Patient patient = new Patient(cpr: cmd.cpr,
                firstName: cmd.firstName,
                lastName: cmd.lastName,
                state: PatientState.ACTIVE,
                sex: cmd.sex,
                address: cmd.address,
                postalCode: cmd.postalCode,
                city: cmd.city,
                phone: cmd.phone,
                mobilePhone: cmd.mobilePhone,
                email: cmd.email,
                comment: cmd.comment
        )
        patient.user = patientUser

        if (!patient.save()) {
            throw new PatientException("patient.not.created", patient.errors)
        }

        //Link to patient group(s)
        cmd.groupIds.each { patientGroupID ->
            PatientGroup patientGroup = PatientGroup.get(patientGroupID)
            Patient2PatientGroup p2pgLink = new Patient2PatientGroup()
            patient.addToPatient2PatientGroups(p2pgLink)
            patientGroup.addToPatient2PatientGroups(p2pgLink)
            if (!p2pgLink.save()) {
                throw new PatientException("patient.patientgroup.not.created", p2pgLink.errors)
            }
        }

        //Add thresholds
        patient.thresholds = cmd.thresholds as Set<Threshold>

        //Attach an empty monitoring plan
        MonitoringPlan monitoringPlanInstance = new MonitoringPlan(patient: patient, startDate: new Date().clearTime())
        patient.monitoringPlan = monitoringPlanInstance

        if (!monitoringPlanInstance.save()) {
            throw new PatientException("patient.create.flow.autoCreateMonPlan.error")
        }

        //Add next of kin(s)
        cmd.nextOfKins.each { NextOfKinPerson nok ->
            patient.addToNextOfKinPersons(nok)
        }

        //prefill blueAlarmQuestionnaireIDs
        patient.blueAlarmQuestionnaireIDs = []

        //Validate, save and return patient object
        patient.save()
        patientOverviewMaintenanceService.createOverviewFor(patient)
        return patient //Return with validation errors, for the view to handle/show
    }

    /**
     * Updates a patient. Handles updating of patient data, resetting og user password and updating patientgroup relationships
     * @param params Input parameters
     * @throws PatientException Thrown when a problem has occurred when updating the patient information
     * @throws PatientGroupException Thrown when a problem has occurred when updating the patient group relationship
     */
    @Transactional
    def updatePatient(def params, Patient patientInstance) {
        if (!patientInstance) {
            throw new PatientNotFoundException()
        }

        if (params.version) {
            def v = params.version.toLong()
            if (patientInstance.version > v) {
                throw new OptimisticLockingException()
            }
        }

        /*
         * Make sure the call is coming from the patient controller,
         * so that the params will include patient data. Otherwise,
         * do not use params as parameters for the update.
         */
        if (params.controller == "patient") {
            patientInstance.properties = params

            patientInstance.cpr = patientInstance.cpr.replaceAll(" ", "")
            patientInstance.cpr = patientInstance.cpr.replaceAll("-", "")
        }

        def user = patientInstance.user
        if (params.cleartextPassword) {
            user.password = params.cleartextPassword
            user.cleartextPassword = params.cleartextPassword
            user.validate(['cleartextPassword'])
        }

        if (params.groupid != null) {

            ArrayList<String> groupId = new ArrayList<String>()
            groupId.addAll(params.groupid)

            def pGroups = []

            groupId.each { id ->
                def pg = PatientGroup.get(id)

                // figure out if there's a mapping.
                if (pg) {
                    pGroups << pg // we need it for later
                    def p2pg = Patient2PatientGroup.findByPatientAndPatientGroup(patientInstance, pg)
                    if (!p2pg) {
                        // Add link
                        Patient2PatientGroup ref = new Patient2PatientGroup()
                        patientInstance.addToPatient2PatientGroups(ref)
                        pg.addToPatient2PatientGroups(ref)
                        ref.save(failOnError: true, flush: true)
                        ref.refresh()
                        if (ref.hasErrors()) {
                            throw new PatientGroupException("patient.patientgroup.not.created", ref.errors)
                        }
                    }
                }
            }

            // remove old references
            def oldp2pgs = Patient2PatientGroup.findAllByPatient(patientInstance)
            oldp2pgs.each { pg ->
                if (!pGroups.contains(pg.patientGroup)) {
                    // Remove it.
                    patientInstance.removeFromPatient2PatientGroups(pg)
                    pg.patientGroup.removeFromPatient2PatientGroups(pg)
                    pg.delete(failOnError: true, flush: true)
                }
            }
        }

        if (params.nextOfKinPersonId) {
            def nok = NextOfKinPerson.get(params.nextOfKinPersonId)
            if (nok) {
                if (!patientInstance.nextOfKinPersons.contains(nok)) {
                    patientInstance.nextOfKinPersons.add(nok)
                }
            } else {
                patientInstance.errors.reject("patient.validate.nextofkin.not.found")
                return patientInstance
            }
        }

        patientInstance.validate()
        if (user.hasErrors()) {
            user.errors.fieldErrors.each {
                patientInstance.errors.rejectValue("user.${it.field}", it.code, it.arguments, it.defaultMessage)
            }
            throw new PatientException("Error")
        }

        patientInstance.save(failOnError: true)
        patientOverviewMaintenanceService.updateOverviewFor(patientInstance)

        return patientInstance
    }

    List<Patient> searchPatient(PatientSearchCommand command) {
        def clinician = Clinician.findByUser(springSecurityService.currentUser as User)
        def patients = Patient.patientSearch(command).list()

        //If we search by phone number, include next of kin phone numbers in the search
        if (command.phone) {
            patients += NextOfKinPerson.findAllByPhone(command.phone)*.patient
        }

        //Only return patients this clinician is allowed to see
        filterPatientsForClinician(clinician, patients)
    }

    def isNoteSeenByUser(PatientNote note) {
        def clinician = Clinician.findByUser(springSecurityService.getCurrentUser())
        return note.seenBy.contains(clinician)
    }

    def allowedToView(Long patientId) {
        if (patientId) {
            def p = Patient.get(patientId)
            return allowedToView(p)
        }
    }

    def allowedToView(Patient patient) {
        def currentUser = springSecurityService.getCurrentUser()

        if (currentUser == null) {
            return false
        }

        def clinician = Clinician.findByUser(currentUser)

        if (clinician == null) {
            def patientUser = Patient.findByUser(currentUser)
            if (patientUser) {
                return patientUser.id == patient.id //Patient can view herself
            } else {
                return false
            }
        } else if (SpringSecurityUtils.ifAnyGranted(PermissionName.PATIENT_READ_ALL_IN_SYSTEM)) {

            return true
        } else {
            def patientGroupsForPatient = Patient2PatientGroup.findAllByPatient(patient).collect { it.patientGroup }
            def patientGroupsForClinician = Clinician2PatientGroup.findAllByClinician(clinician).collect { it.patientGroup }

            // The lines above are two different Hibernate queries and might return both proxy objects and "real" Java
            // objects, so to be able to do "intersect" below, we need to fetch out the ids
            def patientGroupIdsForPatient = patientGroupsForPatient.collect { it.id }
            def patientGroupIdsForClinician = patientGroupsForClinician.collect { it.id }

            def patientGroupIdOverlap = patientGroupIdsForPatient.intersect(patientGroupIdsForClinician)
            !patientGroupIdOverlap.isEmpty()
        }
    }

    def resetPassword(Patient patient) {
        passwordService.resetPassword(patient.user)
    }

    def sendPassword(Patient patient) {
        mailSenderService.sendMail(i18nService.message(code: 'patient.send-password.subject'), patient.email, '/email/passwordRecovery',
                [patient: patient.name, clinician: clinicianService.currentClinician.name, password: patient.user.cleartextPassword])
    }

    @Transactional
    def removeAllBlueAlarms(Patient patient) {
        patient.blueAlarmQuestionnaireIDs = []
        patient.save(failOnError: true)

        patientOverviewMaintenanceService.updateOverviewFor(patient)
    }

    @Transactional
    def noAlarmIfUnreadMessagesToPatient(Patient patient) {
        patient.noAlarmIfUnreadMessagesToPatient = true
        patient.save(failOnError: true)

        patientOverviewMaintenanceService.updateOverviewFor(patient)
    }

    /**
     * Filters patients the clinician is allowed to view. That is, patients that
     * are in patientGroups the clinician is associated with.
     */
    private def filterPatientsForClinician(Clinician clinician, Collection<Patient> patients) {

        if (SpringSecurityUtils.ifAnyGranted(PermissionName.PATIENT_READ_ALL_IN_SYSTEM)) {
            patients
        } else {

            patients.findAll {
                Patient2PatientGroup.findAllByPatient(it)*.patientGroup
                        .intersect(Clinician2PatientGroup.findAllByClinician(clinician)*.patientGroup)
                        .size() > 0
            }.unique()
        }
    }
}
