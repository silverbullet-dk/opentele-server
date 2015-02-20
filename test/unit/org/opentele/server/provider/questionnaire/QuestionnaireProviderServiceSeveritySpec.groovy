package org.opentele.server.provider.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.Patient
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.ScheduleWindow
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.Questionnaire2MeterType
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireNode
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.core.model.types.Severity
import spock.lang.Specification
import spock.lang.Unroll


@TestFor(QuestionnaireProviderService)
@Build([StandardSchedule, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader, QuestionnaireHeader, Questionnaire, QuestionnaireNode, QuestionnaireSchedule, PatientBooleanNode, BooleanNode, ScheduleWindow, PatientQuestionnaire, MonitoringPlan, CompletedQuestionnaire])
@Mock([CompletedQuestionnaire, Questionnaire2MeterType])
class QuestionnaireProviderServiceSeveritySpec extends Specification {

    @Unroll
    def "test that questionnaireOfWorstSeverity returns the correct questionnaire"() {

        setup:
        Patient patient = new PatientBuilder().build()
        def questionnaires = severities.collect{
            new CompletedQuestionnaireBuilder(severity: it).forPatient(patient).build()
        }

        when:
        def result = service.questionnaireOfWorstSeverity(questionnaires)

        then:
        result?.severity == expectedResult

        where:
        severities                                           | expectedResult
        []                                                   | null
        [Severity.GREEN]                                     | Severity.GREEN
        [Severity.GREEN, Severity.ORANGE]                    | Severity.ORANGE
        [Severity.ORANGE, Severity.YELLOW]                   | Severity.YELLOW
        [Severity.YELLOW, Severity.RED]                      | Severity.RED
        [Severity.RED, Severity.BELOW_THRESHOLD]             | Severity.BELOW_THRESHOLD
        [Severity.BELOW_THRESHOLD, Severity.ABOVE_THRESHOLD] | Severity.ABOVE_THRESHOLD
        [Severity.GREEN, Severity.YELLOW]                    | Severity.YELLOW
        [Severity.RED, Severity.ORANGE]                      | Severity.RED
        [Severity.YELLOW, Severity.GREEN]                    | Severity.YELLOW
        [Severity.YELLOW, Severity.BELOW_THRESHOLD]          | Severity.BELOW_THRESHOLD
    }
}
