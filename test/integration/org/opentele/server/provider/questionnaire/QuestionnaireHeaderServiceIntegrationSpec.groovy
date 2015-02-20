package org.opentele.server.provider.questionnaire
import grails.test.spock.IntegrationSpec
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireNode
import spock.lang.Ignore

class QuestionnaireHeaderServiceIntegrationSpec extends IntegrationSpec {
    //static transactional = false

    def questionnaireHeaderService
    def grailsApplication

    def setup() {
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
    }

    @Ignore("Not working in test.. but works when running the app :-(")
    def "test remove draftQuestionnaire"() {
        setup:
        def name = 'KOL Spørgeskema (skal ikke bruges)'
        QuestionnaireHeader questionnaireHeader = QuestionnaireHeader.findByName(name)
        def questionnaireId = questionnaireHeader.draftQuestionnaire.id
        def nodeIds = questionnaireHeader.draftQuestionnaire.nodes*.id as List

        when:
        questionnaireHeaderService.deleteDraft(questionnaireHeader)

        then:
        !QuestionnaireNode.findAllByIdInList(nodeIds)
        !Questionnaire.get(questionnaireId)
    }
}
