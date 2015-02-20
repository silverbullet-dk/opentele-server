package org.opentele.server.provider

import grails.test.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.provider.PatientNoteService

class PatientNoteServiceIntegrationSpec extends IntegrationSpec {
    PatientNoteService patientNoteService

    PatientGroup clinicianPatientGroup
    PatientGroup otherPatientGroup

    Clinician clinician = Clinician.build()

    Patient patient1InPatientGroup
    Patient patient2InPatientGroup
    Patient inactivePatientInPatientGroup
    Patient patientInOtherPatientGroup

    def setup() {
        clinicianPatientGroup = PatientGroup.build()
        otherPatientGroup = PatientGroup.build()

        clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        patient1InPatientGroup = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        patient2InPatientGroup = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        inactivePatientInPatientGroup = createPatient(PatientState.DECEASED, clinicianPatientGroup)
        patientInOtherPatientGroup = createPatient(PatientState.ACTIVE, otherPatientGroup)
    }

    def 'can find patient notes for team'() {
        when:
        patient1InPatientGroup.addToNotes(note: 'Note 1', type: NoteType.NORMAL)
        patient1InPatientGroup.addToNotes(note: 'Note 2', type: NoteType.NORMAL)
        patient2InPatientGroup.addToNotes(note: 'Note 3', type: NoteType.NORMAL)
        inactivePatientInPatientGroup.addToNotes(note: 'Note 4', type: NoteType.NORMAL)
        patientInOtherPatientGroup.addToNotes(note: 'Note 5', type: NoteType.NORMAL)

        then:
        patientNoteService.patientNotesForTeam(clinician)*.note.toSet() == ['Note 1', 'Note 2', 'Note 3'].toSet()
    }

    def 'can find IDs of seen patient notes'() {
        when:
        PatientNote seenNote1 = PatientNote.build(patient: patient1InPatientGroup, note: 'Note 1', type: NoteType.NORMAL, seenBy: [clinician])
        PatientNote seenNote2 = PatientNote.build(patient: patient2InPatientGroup, note: 'Note 1', type: NoteType.NORMAL, seenBy: [clinician])
        PatientNote unseenNote1 = PatientNote.build(patient: patient1InPatientGroup, note: 'Note 2', type: NoteType.NORMAL, seenBy: [])
        PatientNote unseenNote2 = PatientNote.build(patient: patient2InPatientGroup, note: 'Note 3', type: NoteType.NORMAL, seenBy: [])
        Set<PatientNote> allNotes = [seenNote1, seenNote2, unseenNote1, unseenNote2].toSet()

        then:
        patientNoteService.idsOfSeenPatientNotes(clinician, allNotes) == [seenNote1.id, seenNote2.id].toSet()
    }

    def 'can find out if a note has been seen or not'() {
        when:
        PatientNote seenNote1 = PatientNote.build(patient: patient1InPatientGroup, note: 'Note 1', type: NoteType.NORMAL, seenBy: [clinician])
        PatientNote seenNote2 = PatientNote.build(patient: patient2InPatientGroup, note: 'Note 1', type: NoteType.NORMAL, seenBy: [clinician])
        PatientNote unseenNote1 = PatientNote.build(patient: patient1InPatientGroup, note: 'Note 2', type: NoteType.NORMAL, seenBy: [])
        PatientNote unseenNote2 = PatientNote.build(patient: patient2InPatientGroup, note: 'Note 3', type: NoteType.NORMAL, seenBy: [])
        Set<PatientNote> allNotes = [seenNote1, seenNote2, unseenNote1, unseenNote2].toSet()

        then:
        patientNoteService.isNoteSeenByAnyUser(seenNote1) == true
        patientNoteService.isNoteSeenByAnyUser(seenNote2) == true
        patientNoteService.isNoteSeenByAnyUser(unseenNote1) == false
        patientNoteService.isNoteSeenByAnyUser(unseenNote2) == false
    }

    private Patient createPatient(PatientState state, PatientGroup group) {
        Patient patient = Patient.build(state: state)
        patient.addToPatient2PatientGroups(patientGroup: group)

        patient
    }
}
