package org.opentele.builders

import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.core.model.types.Severity

class CompletedQuestionnaireBuilder {
    Patient patient
    def patientQuestionnaire
    def name
    def questionnaireHeader
    def uploadDate = new Date()
    def receivedDate = new Date()
    Severity severity = Severity.GREEN
    def createdDate = new Date()
    boolean acknowledged


    CompletedQuestionnaireBuilder forPatient(Patient patient) {
        this.patient = patient
        this
    }

    CompletedQuestionnaireBuilder forName(String name) {
        this.name = name
        this
    }

    CompletedQuestionnaireBuilder acknowledged() {
        this.acknowledged = true
        this
    }

    CompletedQuestionnaireBuilder uploadedAt(Date uploadDate) {
        this.uploadDate = uploadDate
        this
    }

    CompletedQuestionnaire build() {
        this.patientQuestionnaire = new PatientQuestionnaireBuilder().build()

        QuestionnaireHeaderBuilder questionnaireHeaderBuilder = new QuestionnaireHeaderBuilder()
        if (name != null) {
            questionnaireHeaderBuilder = questionnaireHeaderBuilder.forName(name)
        }
        this.questionnaireHeader = questionnaireHeaderBuilder.build()

        def result = new CompletedQuestionnaire(patient: patient, questionnaireHeader: questionnaireHeader,
                patientQuestionnaire: patientQuestionnaire, uploadDate: uploadDate, receivedDate: receivedDate, severity: severity, createdDate: createdDate)
        if (acknowledged) {
            Clinician clinician = Clinician.build()
            result.acknowledgedBy = clinician
            result.acknowledgedDate = new Date()
        }

        result.save(failOnError: true)

        result
    }

    CompletedQuestionnaireBuilder ofSeverity(Severity severity) {
        this.severity = severity
        this
    }
}
