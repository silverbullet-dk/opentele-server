package org.opentele.server.provider.model.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.core.model.types.Severity
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.provider.AcknowledgeQuestionnaireService
import spock.lang.Specification

@TestFor(QuestionnaireController)
@Build([CompletedQuestionnaire, Patient])
class QuestionnaireControllerSpec extends Specification{

    CompletedQuestionnaire questionnaireA

    def setup() {

        controller.acknowledgeQuestionnaireService = Mock(AcknowledgeQuestionnaireService)

        questionnaireA = CompletedQuestionnaire.build(patient: Patient.build(firstName: "PatientA"), acknowledgedDate: new Date(), severity:  Severity.GREEN)
    }

    def "test acknowledge makes acknowledgement available to patient"() {
        setup:
        def cq = questionnaireA
        params.id = cq.id
        params.note = ""
        params.withAutoMessage = "true"
        controller.acknowledgeQuestionnaireService.acknowledge(_, _, true) >> cq

        when:
        controller.acknowledge()

        then:
        flash.message == "completedquestionnaire.acknowledged"
    }
}
