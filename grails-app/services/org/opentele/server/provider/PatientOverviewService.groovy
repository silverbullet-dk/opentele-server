package org.opentele.server.provider

import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.model.PatientOverview
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire

class PatientOverviewService {
    def questionnaireService
    def grailsApplication

    List<PatientOverview> getPatientsForClinicianOverview(Clinician activeClinician) {

        long time = System.currentTimeMillis()

        def importantPatientOverviewIds = PatientOverview.executeQuery(
                'select po.id ' +
                        'from PatientOverview as po ' +
                        'inner join po.patient as p ' +
                        'inner join p.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'inner join pg.clinician2PatientGroups as c2pg ' +
                        'where po.important = true ' +
                        '  and c2pg.clinician.id = ?', [activeClinician.id])

        log.warn "So far 1: ${System.currentTimeMillis() - time}"
        time = System.currentTimeMillis()

        // Find all patients with reminders not seen by clinician. Since this depends on the current clinician,
        // it cannot be embedded in the PatientOverview object for each patient.
        def idsOfPatientOverviewsWithRemindersNotSeenByClinician = PatientOverview.executeQuery(
                'select po.id ' +
                        'from Patient as p ' +
                        'inner join p.patientOverviews as po ' +
                        'inner join p.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'inner join pg.clinician2PatientGroups as c2pg ' +
                        'where c2pg.clinician.id = ? ' +
                        '  and p.state = ? '+
                        '  and exists (from p.notes note where note.reminderDate < ? and ? not in (select id from note.seenBy))',
                [activeClinician.id, PatientState.ACTIVE, new Date(), activeClinician.id])

        log.warn  "So far 2: ${System.currentTimeMillis() - time}"
        time = System.currentTimeMillis()

        importantPatientOverviewIds.addAll(idsOfPatientOverviewsWithRemindersNotSeenByClinician)


        if(grailsApplication.config.patientoverview.use.simple.sort) {
            def result = PatientOverview.findAllByIdInList(importantPatientOverviewIds).sort { a, b ->
                b.questionnaireSeverity <=> a.questionnaireSeverity
            }
            log.warn  "So far 3: ${System.currentTimeMillis() - time}"
            time = System.currentTimeMillis()

            result
        } else {
            def criteria  = PatientOverview.createCriteria()
            def result = criteria.list {
                'in'('id', importantPatientOverviewIds)
                order('questionnaireSeverityOrdinal', 'desc')
                order('numberOfUnreadMessagesFromPatient', 'desc')
                order('numberOfUnreadMessagesToPatient', 'desc')
                order('name', 'asc')

            }
            log.warn  "So far 4: ${System.currentTimeMillis() - time}"

            result
        }

    }

    boolean isClinicianPartOfPatientGroup(Clinician activeClinician, PatientGroup activePatientGroup) {
        return activeClinician.clinician2PatientGroups.find { it.patientGroup == activePatientGroup }
    }

    List<PatientOverview> getPatientsForClinicianOverviewInPatientGroup(Clinician activeClinician, PatientGroup activePatientGroup) {
        if (!isClinicianPartOfPatientGroup(activeClinician, activePatientGroup)) {
            throw new IllegalArgumentException("Clinician ${activeClinician} is not part of given patient group (${activePatientGroup})")
        }

        def importantPatientOverviewIds = PatientOverview.executeQuery(
                'select po.id ' +
                        'from PatientOverview as po ' +
                        'inner join po.patient as p ' +
                        'inner join p.patient2PatientGroups as p2pg ' +
                        'where po.important = true ' +
                        '  and p2pg.patientGroup.id = ?',
                [activePatientGroup.id]
        )

        // Find all patients with reminders not seen by clinician. Since this depends on the current clinician,
        // it cannot be embedded in the PatientOverview object for each patient.
        def idsOfPatientOverviewsWithRemindersNotSeenByClinician = PatientOverview.executeQuery(
                'select po.id ' +
                        'from Patient as p ' +
                        'inner join p.patientOverviews as po ' +
                        'inner join p.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'where pg.id = ? ' +
                        '  and p.state = ? '+
                        '  and exists (from p.notes note where note.reminderDate < ? and ? not in (select id from note.seenBy))',
                [activePatientGroup.id, PatientState.ACTIVE, new Date(), activeClinician.id]
        )

        importantPatientOverviewIds.addAll(idsOfPatientOverviewsWithRemindersNotSeenByClinician)

        if(grailsApplication.config.patientoverview.use.simple.sort) {
            PatientOverview.findAllByIdInList(importantPatientOverviewIds).sort { a, b ->
                b.questionnaireSeverity <=> a.questionnaireSeverity
            }
        } else {
            if (importantPatientOverviewIds != null && importantPatientOverviewIds.size() > 0) {

                def criteria  = PatientOverview.createCriteria()
                criteria.list {
                    'in'('id', importantPatientOverviewIds)
                    order('questionnaireSeverityOrdinal', 'desc')
                    order('numberOfUnreadMessagesFromPatient', 'desc')
                    order('numberOfUnreadMessagesToPatient', 'desc')
                    order('name', 'asc')
                }
            } else {
                return Collections.EMPTY_LIST
            }
        }
    }

    Set<Long> getIdsOfPatientsWithMessagingEnabled(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return Collections.emptySet()
        }

        PatientOverview.executeQuery(
                'select patient.id ' +
                        'from Patient as patient ' +
                        'inner join patient.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'inner join pg.clinician2PatientGroups as c2pg ' +
                        'where c2pg.clinician = :clinician ' +
                        '  and patient.id in :patientIds '+
                        '  and pg.disableMessaging = false',
                [clinician: clinician, patientIds: patientOverviews*.patientId]
        ).toSet()
    }

    Set<Long> getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return Collections.emptySet()
        }

        PatientOverview.executeQuery(
                'select patient.id ' +
                        'from Patient as patient ' +
                        'inner join patient.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'inner join pg.clinician2PatientGroups as c2pg ' +
                        'where c2pg.clinician = :clinician ' +
                        '  and patient.id in :patientIds '+
                        '  and patient.noAlarmIfUnreadMessagesToPatient = true',
                [clinician: clinician, patientIds: patientOverviews*.patientId]
        ).toSet()
    }

    Map<Long, List<PatientNote>> fetchUnseenNotesForPatients(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return [:]
        }

        List<PatientNote> patientNotes = PatientNote.executeQuery(
                'select note ' +
                        'from PatientNote as note ' +
                        'inner join note.patient as patient ' +
                        'where patient.id in :patientIds ' +
                        '  and :clinicianId not in (select id from note.seenBy)',
                [patientIds: patientOverviews*.patientId, clinicianId: clinician.id]
        )

        Map<Long, List<PatientNote>> result = [:]
        patientOverviews.each { result[it.patientId] = [] }
        patientNotes.each { result[it.patientId] << it }
        result
    }
}
