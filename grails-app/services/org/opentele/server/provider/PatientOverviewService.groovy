package org.opentele.server.provider

import org.opentele.server.model.Clinician
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.model.PatientOverview
import org.opentele.server.core.model.types.PatientState

class PatientOverviewService {
    def questionnaireService

    def defaultOffset = 0
    def defaultMax = 10
    def maxCapacity = 100

    int count(Clinician activeClinician, PatientGroup activePatientGroup) {

        PatientOverview.withSession { session ->

            def countQueryString = """
                    SELECT DISTINCT count(po)
                    FROM PatientOverview AS po
                    INNER JOIN po.patient AS p
                    INNER JOIN p.patient2PatientGroups AS p2pg
                    INNER JOIN p2pg.patientGroup AS pg"""
            if (activePatientGroup != null) {
                countQueryString += """
                        WHERE (p2pg.patientGroup.id = :activePatientGroupId
                                  AND po.important = true)
                              OR
                              (pg.id = :activePatientGroupId AND
                                  p.state = :patientState AND
                                  EXISTS (FROM p.notes note
                                             WHERE note.reminderDate < :date
                                                   AND :activeClinicianId NOT IN (SELECT id FROM note.seenBy)))"""
            } else {
                countQueryString += """
                        INNER JOIN pg.clinician2PatientGroups AS c2pg
                        WHERE c2pg.clinician.id = :activeClinicianId
                              AND
                             (  (po.important = true)
                                OR
                                (p.state = :patientState
                                    AND EXISTS (FROM p.notes note
                                                WHERE note.reminderDate < :date
                                                      AND
                                                      :activeClinicianId NOT IN (SELECT id FROM note.seenBy))))"""
            }

            def countPatientOverviewQuery = session.createQuery(countQueryString)
            countPatientOverviewQuery = countPatientOverviewQuery
                    .setParameter("activeClinicianId", activeClinician.id)
                    .setParameter("patientState", PatientState.ACTIVE)
                    .setParameter("date" , new Date())
            if (activePatientGroup != null) {
                countPatientOverviewQuery = countPatientOverviewQuery.setParameter("activePatientGroupId", activePatientGroup.id)
            }

            countPatientOverviewQuery.list().get(0)
        }
    };

    List<PatientOverview> getPatientsForClinicianOverview(Clinician activeClinician, Map params) {

        def offset = params.offset ? params.int('offset') : defaultOffset
        def max = Math.min(params.max ? params.int('max') : defaultMax, maxCapacity)

        PatientOverview.withSession { session ->

            def importantPatientOverviewQuery = session.createQuery("""
                    SELECT DISTINCT po
                    FROM PatientOverview AS po
                    INNER JOIN po.patient AS p
                    INNER JOIN p.patient2PatientGroups AS p2pg
                    INNER JOIN p2pg.patientGroup AS pg
                    INNER JOIN pg.clinician2PatientGroups AS c2pg
                    WHERE c2pg.clinician.id = :activeClinicianId
                          AND
                          (   (po.important = true)
                              OR
                              (p.state = :patientState
                                  AND EXISTS (FROM p.notes note
                                              WHERE note.reminderDate < :date
                                                    AND
                                                    :activeClinicianId NOT IN (SELECT id FROM note.seenBy))))
                    ORDER BY po.questionnaireSeverityOrdinal DESC, po.name ASC""")

            def importantPatientOverviews = importantPatientOverviewQuery
                    .setParameter("activeClinicianId", activeClinician.id)
                    .setParameter("patientState", PatientState.ACTIVE)
                    .setParameter("date" , new Date())
                    .setFirstResult(offset)
                    .setMaxResults(max)
                    .list()

            importantPatientOverviews
        }
    }

    boolean isClinicianPartOfPatientGroup(Clinician activeClinician, PatientGroup activePatientGroup) {
        return activeClinician.clinician2PatientGroups.find { it.patientGroup == activePatientGroup }
    }

    List<PatientOverview> getPatientsForClinicianOverviewInPatientGroup(Clinician activeClinician, Map params, PatientGroup activePatientGroup) {

        if (!isClinicianPartOfPatientGroup(activeClinician, activePatientGroup)) {
            throw new IllegalArgumentException("Clinician ${activeClinician} is not part of given patient group (${activePatientGroup})")
        }

        def offset = params.offset ? params.int('offset') : defaultOffset
        def max = params.min = Math.min(params.max ? params.int('max') : defaultMax, maxCapacity)

        PatientOverview.withSession { session ->

            def importantPatientOverviewQuery = session.createQuery("""
                    SELECT po
                    FROM PatientOverview AS po
                    INNER JOIN po.patient AS p
                    INNER JOIN p.patient2PatientGroups AS p2pg
                    INNER JOIN p2pg.patientGroup AS pg
                    WHERE (p2pg.patientGroup.id = :activePatientGroupId
                          AND po.important = true)
                          OR
                          (pg.id = :activePatientGroupId AND
                          p.state = :patientState AND
                          EXISTS (FROM p.notes note
                                  WHERE note.reminderDate < :date
                                  AND :activeClinicianId NOT IN (SELECT id FROM note.seenBy)))
                    ORDER BY po.questionnaireSeverityOrdinal DESC, po.name ASC""")

            def importantPatientOverviews = importantPatientOverviewQuery
                    .setParameter("activePatientGroupId", activePatientGroup.id)
                    .setParameter("activeClinicianId", activeClinician.id)
                    .setParameter("patientState", PatientState.ACTIVE)
                    .setParameter("date" , new Date())
                    .setFirstResult(offset)
                    .setMaxResults(max)
                    .list()

            importantPatientOverviews
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
