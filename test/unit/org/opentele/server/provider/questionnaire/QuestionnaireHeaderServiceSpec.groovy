package org.opentele.server.provider.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.core.QuestionnaireService
import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuestionnaireHeaderService)
@Build([QuestionnaireHeader, Questionnaire])
class QuestionnaireHeaderServiceSpec extends Specification {
    QuestionnaireHeader questionnaireHeader

    def setupSpec() {
    }

    def setup() {
        service.questionnaireService = Mock(QuestionnaireService)
        service.questionnaireProviderService = Mock(QuestionnaireProviderService)
        questionnaireHeader = QuestionnaireHeader.build()
        def questionnaire1 = Questionnaire.build(questionnaireHeader: questionnaireHeader)
        def questionnaire2 = Questionnaire.build(questionnaireHeader: questionnaireHeader)
        def questionnaire3 = Questionnaire.build(questionnaireHeader: questionnaireHeader)
        [questionnaire1, questionnaire2, questionnaire3].each { questionnaireHeader.addToQuestionnaires(it) }
        questionnaireHeader.save(flush: true)
    }

    @Unroll
    def "when requesting historic questionnaires, active and draft questionnaire should not be in the list"() {
        setup:
        questionnaireHeader.activeQuestionnaire = Questionnaire.get(active)
        questionnaireHeader.draftQuestionnaire = Questionnaire.get(draft)

        when:
        def historic = service.findHistoricQuestionnaires(questionnaireHeader)

        then:
        historic*.id == expectedHistoric

        where:
        active | draft | expectedHistoric
        null   | null  | [1, 2, 3]
        1      | null  | [2, 3]
        null   | 1     | [2, 3]
        1      | 2     | [3]
    }

    def "when unpublishing the active questionnaire the questionnaireheader should not have an active questionnaire afterwards"() {
        setup:
        questionnaireHeader.activeQuestionnaire = Questionnaire.get(1)

        when:
        service.unpublishActive(questionnaireHeader)

        then:
        !questionnaireHeader.activeQuestionnaire
    }

    @Unroll
    def "when publishing a draft, the active questionnaire should change and draft should be null"() {
        setup:
        questionnaireHeader.activeQuestionnaire = Questionnaire.get(active)
        questionnaireHeader.draftQuestionnaire = Questionnaire.get(draft)
        Clinician creator = null

        when:
        service.publishDraft(questionnaireHeader, creator)

        then:
        expectedCalls * service.questionnaireService.createPatientQuestionnaire(_, Questionnaire.get(draft))
        !questionnaireHeader.draftQuestionnaire
        questionnaireHeader.activeQuestionnaire == Questionnaire.get(expectedActiveId)

        where:
        active | draft | expectedCalls | expectedActiveId
        null   | null  | 0             | null
        1      | null  | 0             | 1
        null   | 1     | 1             | 1
        1      | 2     | 1             | 2
    }
}
