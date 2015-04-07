package org.opentele.server.provider.model.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.provider.questionnaire.QuestionnaireHeaderService
import spock.lang.Specification

@TestFor(QuestionnaireHeaderController)
@Build([QuestionnaireHeader, Questionnaire])
class QuestionnaireHeaderControllerSpec extends Specification {


    def setup() {
        controller.questionnaireHeaderService = Mock(QuestionnaireHeaderService)
    }

    def "test create method"(){

        when:
        def model = controller.create()

        then:
        model.questionnaireHeaderInstance != null
        response.redirectedUrl == null
    }

    def "test list method"(){

        when:
        def model = controller.list()

        then:
        model.questionnaireHeaderInstanceList.size() == 0
        model.questionnaireHeaderInstanceTotal == 0
    }

    def "test index method"(){

        when:
        controller.index()

        then:
        "/questionnaireHeader/list" == response.redirectedUrl
    }

    def "test save action with no params"() {

        setup:
        request.method = 'POST'

        when:
        controller.save("test")

        then:
        flash.message == "default.created.message"
        response.redirectedUrl == '/questionnaireHeader/show/1'
        QuestionnaireHeader.count() == 1
    }

    def "test save action with params given"() {

        setup:
        request.method = 'POST'
        params.requiresManualInspection = "ON"
        params.name = "test"

        when:
        controller.save("test")

        then:
        response.redirectedUrl == '/questionnaireHeader/show/1'
        QuestionnaireHeader.count() == 1
        QuestionnaireHeader.get(1).name == "test"
        QuestionnaireHeader.get(1).requiresManualInspection == true
    }

    def "test saveAndEdit action with params given"() {

        setup:
        request.method = 'POST'
        params.requiresManualInspection = "ON"
        params.name = "test"

        when:
        controller.saveAndEdit("test")

        then:
        response.redirectedUrl == '/questionnaireHeader/doCreateDraft/1'
        QuestionnaireHeader.count() == 1
        QuestionnaireHeader.get(1).name == "test"
        QuestionnaireHeader.get(1).requiresManualInspection == true
    }

    def "test save not setting manual inspection require"() {

        setup:
        request.method = 'POST'
        params.name = "test"

        when:
        controller.save("test")

        then:
        response.redirectedUrl == '/questionnaireHeader/show/1'
        QuestionnaireHeader.count() == 1
        QuestionnaireHeader.get(1).name == "test"
        QuestionnaireHeader.get(1).requiresManualInspection == false
    }

    def "test show"() {

        setup:
        QuestionnaireHeader qh = QuestionnaireHeader.build()
        request.method = 'POST'
        params.id = qh.id

        when:
        def model = controller.show()

        then:
        model.questionnaireHeaderInstance.id == qh.id
        QuestionnaireHeader.get(1).requiresManualInspection == false
    }

    def "test update in sunshine scenario"() {

        setup:
        QuestionnaireHeader questionnaireHeader = QuestionnaireHeader.build(name: "Dummy")

        request.method = 'POST'

        when:
        params.id = questionnaireHeader.id
        params.name = "Dummy"

        and:
        controller.update()

        then:
        response.redirectedUrl == "/questionnaireHeader/show/${questionnaireHeader.id}"
        flash.message == "default.updated.message"
    }
}
