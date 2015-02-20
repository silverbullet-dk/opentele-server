package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientNote
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class PatientNoteController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def springSecurityService
    def sessionService
    def patientService
    def clinicianService
    def patientNoteService

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        Patient patient = Patient.get(params.id)
        sessionService.setPatient(session, patient)

        def notes = PatientNote.findAllByPatient(patient, [sort: 'createdDate', order: 'desc', max: params.max, offset: params.offset])
        def isSeen = notes.collectEntries { [it, patientService.isNoteSeenByUser(it)] }
        def isSeenByAnyUser = notes.collectEntries { [it, patientNoteService.isNoteSeenByAnyUser(it)] }
        sortNotes(notes, isSeen, isSeenByAnyUser)

        [
                patientNoteInstanceList: notes,
                isSeen: isSeen,
                isSeenByAnyUser: isSeenByAnyUser,
                patientNoteInstanceTotal: patient.notes.size(),
                patient: patient
        ]
    }

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL_TEAM)
    def listTeam() {
        Clinician clinician = clinicianService.currentClinician
        sessionService.setNoPatient(session)

        Set<PatientNote> notes = patientNoteService.patientNotesForTeam(clinician)
        Set<Long> idsOfSeenNotes = patientNoteService.idsOfSeenPatientNotes(clinician, notes)

        def isSeen = notes.collectEntries { [it, idsOfSeenNotes.contains(it.id)] }
        def isSeenByAnyUser = notes.collectEntries { [it, patientNoteService.isNoteSeenByAnyUser(it)] }

        List<PatientNote> sortedNotes = notes.toList()
        sortNotes(sortedNotes, isSeen, isSeenByAnyUser)

        [
                patientNoteInstanceList: sortedNotes,
                isSeen: isSeen,
                isSeenByAnyUser: isSeenByAnyUser,
                patientNoteInstanceTotal: sortedNotes.size()
        ]
    }

    private List<PatientNote> sortNotes(List<PatientNote> notes, Map<PatientNote, Boolean> isSeen, Map<PatientNote, Boolean> isSeenByAnyUser) {
        switch (params.sort) {
            case 'note':
                notes.sort { it.note }
                break;
            case 'type':
                notes.sort { it.type.toString() }
                break;
            case 'reminderDate':
                notes.sort { it.reminderDate }
                break;
            case 'isSeen':
                notes.sort { isSeen[it] }
                break;
            case 'isNoteSeenByAnyUser':
                notes.sort { isSeenByAnyUser[it] }
                break;
            case 'patient':
            default:
                notes.sort { it.patient.name }
        }
        if (params.order == 'desc') {
            notes.reverse(true)
        }
    }

    @Secured(PermissionName.PATIENT_NOTE_CREATE)
    def create() {
        Patient p = Patient.get(params.patientId)
        sessionService?.setPatient(session, p)
        [patientNoteInstance: new PatientNote(params), patient:  p]
    }

    @Secured(PermissionName.PATIENT_NOTE_CREATE)
    def save() {
        params.note = params.note.encodeAsHTML()
        params.reminderDate = params.datePicker

        def patientNoteInstance = new PatientNote(params)
        patientNoteInstance.patient = Patient.get(params.patientId)

        if (!isReminderDateFieldsValid(params)) {

            patientNoteInstance.errors.reject(
                    'patientnote.reminderdate.error',
                    [] as Object[],
                    "Påmindelsesdato feltet skal enten være helt udfyldt eller slet ikke udfyldt"
            )
        }

        if (patientNoteInstance.hasErrors() || !patientNoteInstance.save(flush: true)) {
            render(view: "create", model: [patientNoteInstance: patientNoteInstance, patient: patientNoteInstance.patient])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), patientNoteInstance.id])
        redirect(action: "show", id: patientNoteInstance.id)
    }

    @Secured(PermissionName.PATIENT_NOTE_READ)
    def show() {
        def patientNoteInstance = PatientNote.get(params.id)
        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }
        def canEdit = !patientNoteService.isNoteSeenByAnyUser(patientNoteInstance)
        if (!canEdit) {
            flash.message = message(code: 'patientNote.cannotEditSeenNotes')
        }

        sessionService.setPatient(session, patientNoteInstance.patient)
        // Maintain which view show is called from
        if (params.comingFrom) {
            [patientNoteInstance: patientNoteInstance, comingFrom: params.comingFrom, canEdit: canEdit]
        } else {
            [patientNoteInstance: patientNoteInstance, canEdit: canEdit]
        }
    }

    @Secured(PermissionName.PATIENT_NOTE_MARK_SEEN)
    def markSeen() {
        def patientNoteInstance = PatientNote.get(params.id)
        def user = springSecurityService.currentUser
        if (user && user.isClinician() && patientNoteInstance) {
            def clinician = Clinician.findByUser(user)
            patientNoteInstance.seenBy.add(clinician)
        }

        render(view: "show", model: [patientNoteInstance: patientNoteInstance])
    }

    @Secured(PermissionName.PATIENT_NOTE_WRITE)
    def edit() {
        def patientNoteInstance = PatientNote.get(params.id)
        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        if (patientNoteService.isNoteSeenByAnyUser(patientNoteInstance)) {
            flash.message = message(code: 'patientNote.cannotEditSeenNote')
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)

            redirect(action: "show", id: patientNoteInstance.id)
            return
        }

        sessionService.setPatient(session, patientNoteInstance.patient)
        [patientNoteInstance: patientNoteInstance]
    }

    @Secured(PermissionName.PATIENT_NOTE_WRITE)
    def update() {
        def patientNoteInstance = PatientNote.get(params.id)

        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (patientNoteInstance.version > version) {
                patientNoteInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'patientNote.label')] as Object[],
                        "Another user has updated this PatientNote while you were editing")
                render(view: "edit", model: [patientNoteInstance: patientNoteInstance])
                return
            }
        }

        // TODO - noget rod at bruge forretningsobjektet til at holde ikke-validerede props,
        // og til at føre disse tilbage til GUI. Herved overskrives invalide data :-()
        params.reminderDate = params.datePicker
        patientNoteInstance.properties = params

        if (!isReminderDateFieldsValid(params)) {

            patientNoteInstance.errors.reject(message(code:'patientnote.reminderdate.error'))
        }

        if (patientNoteInstance.hasErrors() || !patientNoteInstance.save(flush: true)) {
            render(view: "edit", model: [patientNoteInstance: patientNoteInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'patientNote.label'), patientNoteInstance.id])
        redirect(action: "show", id: patientNoteInstance.id)
    }

    private boolean isReminderDateFieldsValid(def params) {

        //Either all filled out or all empty
        ((params.reminderDate_day?.trim() &&
                params.reminderDate_month?.trim() &&
                params.reminderDate_year?.trim() &&
                params.reminderDate_hour?.trim() &&
                params.reminderDate_minute?.trim())
                || (!params.reminderDate_day?.trim() &&
                !params.reminderDate_month?.trim() &&
                !params.reminderDate_year?.trim() &&
                !params.reminderDate_hour?.trim() &&
                !params.reminderDate_minute?.trim()))
    }

    @Secured(PermissionName.PATIENT_NOTE_DELETE)
    def delete() {
        def patientNoteInstance = PatientNote.get(params.id)

        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }
        if (patientNoteService.isNoteSeenByAnyUser(patientNoteInstance)) {
            flash.message = message(code: 'patientNote.cannotDeleteSeenNote')
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)

            redirect(action: "show", id: patientNoteInstance.id)
            return
        }
        try {
            patientNoteInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'patientNote.label'), params.id])

            // If coming from team note list
            if (params.comingFrom && params.comingFrom == 'listTeam') {
                redirect(action: "listTeam")
            } else {
                redirect(action: "list", id: patientNoteInstance.patient.id)
            }
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'patientNote.label'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
