package org.opentele.server.provider.questionnaire
import grails.test.spock.IntegrationSpec
import org.opentele.server.core.ResultTableViewModel
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.Questionnaire
import spock.lang.Ignore

class QuestionnaireProviderServiceIntegrationSpec extends IntegrationSpec {
    def questionnaireService
    def grailsApplication

    def setup() {

        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
    }

    def "test create patient questionnaire"() {
        setup:
        Clinician helle = Clinician.findByFirstNameAndLastName("Helle", "Andersen")

            // Retrieve q from db
        Questionnaire theq = Questionnaire.findByNameAndRevision("Radioknap test", "0.1")

        when:
        PatientQuestionnaire pq = questionnaireService.createPatientQuestionnaire(helle, theq);

        then:
        pq.templateQuestionnaire.id == theq.id
    }

    def "test remove draft questionnaire"() {
    }

    @Ignore("Should not depend on exact counts")
    def "extract completedQuestionnaires with answers"() {

        setup:
        Patient nancy = Patient.findByFirstNameAndLastName("Nancy Ann", "Berggren")

        when:
        ResultTableViewModel result = questionnaireService.extractCompletedQuestionnaireWithAnswers(nancy.id, null, true, null)

        then:
        result.questions.size() == 888
        result.columnHeaders.size() == 203
        result.results.size() == 208
    }

    def "Even if input is empty questionnaire-filter, method should retrieve something"() {

        setup:
        Patient nancy = Patient.findByFirstNameAndLastName("Nancy Ann", "Berggren")

        when:
        ResultTableViewModel result = questionnaireService.extractCompletedQuestionnaireWithAnswers(nancy.id, [], true, null)

        then:
        result.questions.size() > 0
        result.columnHeaders.size() > 0
        result.results.size() > 0
    }
}
