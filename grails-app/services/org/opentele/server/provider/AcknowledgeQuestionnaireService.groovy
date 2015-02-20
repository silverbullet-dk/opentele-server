package org.opentele.server.provider

import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.springframework.transaction.annotation.Transactional

class AcknowledgeQuestionnaireService {

    def clinicianService
    def patientOverviewMaintenanceService

    @Transactional
    CompletedQuestionnaire acknowledge(CompletedQuestionnaire completedQuestionnaire, String note, boolean sendAcknowledgeMessage = false) {
        Clinician clinician = clinicianService.currentClinician
        acknowledgeQuestionnaireWithoutUpdatingPatientOverview(clinician, completedQuestionnaire, note, sendAcknowledgeMessage)
        patientOverviewMaintenanceService.updateOverviewFor(completedQuestionnaire.patient)
    }

    @Transactional
    def acknowledge(List<CompletedQuestionnaire> completedQuestionnaires, boolean sendAcknowledgeMessage = false) {
        Clinician clinician = clinicianService.currentClinician
        Set<Patient> patientsToUpdate = []

        completedQuestionnaires.each { questionnaire ->
            acknowledgeQuestionnaireWithoutUpdatingPatientOverview(clinician, questionnaire, null, sendAcknowledgeMessage)
            patientsToUpdate << questionnaire.patient
        }

        patientsToUpdate.each { patientOverviewMaintenanceService.updateOverviewFor(it) }
    }

    private void acknowledgeQuestionnaireWithoutUpdatingPatientOverview(Clinician clinician, CompletedQuestionnaire completedQuestionnaire, String note, boolean sendAcknowledgeMessage) {
        completedQuestionnaire.refresh()

        completedQuestionnaire.acknowledgedBy = clinician
        completedQuestionnaire.acknowledgedDate = new Date()
        completedQuestionnaire.showAcknowledgementToPatient = sendAcknowledgeMessage

        if (note) {
            completedQuestionnaire.acknowledgedNote = note
        }
        completedQuestionnaire.save(failOnError: true, flush: true)
    }
}
