package org.opentele.server.provider

import org.opentele.server.model.Clinician
import org.opentele.server.model.PatientNote
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.core.model.types.PatientState

class PatientNoteService {
    Set<PatientNote> patientNotesForTeam(Clinician clinician) {
        PatientNote.withCriteria {
            patient {
                eq('state', PatientState.ACTIVE)

                patient2PatientGroups {
                    patientGroup {
                        clinician2PatientGroups {
                            eq('clinician', clinician)
                        }
                    }
                }
            }
        }.toSet()
    }

    Set<Long> idsOfSeenPatientNotes(Clinician clinician, Set<PatientNote> patientNotes) {
        if (patientNotes.empty) {
            return Collections.emptySet()
        }

        PatientNote.executeQuery(
                'select note.id ' +
                'from PatientNote note ' +
                'where note.id in :patientNoteIds' +
                '  and :clinicianId in (select id from note.seenBy)',
                [patientNoteIds: patientNotes*.id, clinicianId: clinician.id]
        ).toSet()
    }

    def isNoteSeenByAnyUser(PatientNote note) {
        return !note.seenBy.empty
    }

    def countImportantWithReminder(List<PatientNote> notes) {
        if(notes == null) {
            return 0
        }
        notes.count { it.type == NoteType.IMPORTANT && PatientNote.isRemindToday(it) }
    }

    def countNormalWithReminder(List<PatientNote> notes) {
        if(notes == null) {
            return 0
        }
        notes.count { it.type == NoteType.NORMAL && PatientNote.isRemindToday(it) }
    }

    def countImportantWithoutDeadline(List<PatientNote> notes) {
        if(notes == null) {
            return 0
        }
        notes.count { it.type == NoteType.IMPORTANT && !it.reminderDate }
    }
}
