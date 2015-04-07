package org.opentele.server.provider.model.questionnaire

import grails.buildtestdata.mixin.Build
import grails.validation.ValidationErrors
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.server.core.QuestionnaireEditorService
import org.opentele.server.core.command.QuestionnaireEditorCommand
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.provider.ClinicianService
import spock.lang.Specification
import grails.test.mixin.*

@TestFor(QuestionnaireEditorController)
@Build([QuestionnaireHeader, Questionnaire])
class QuestionnaireEditorControllerSpec extends Specification {

    def templateModel
    def questionnaireEditorService
    def clinicianService

    def setup() {
        questionnaireEditorService = Mock(QuestionnaireEditorService)
        clinicianService = Mock(ClinicianService)
        controller.questionnaireEditorService = questionnaireEditorService
        controller.clinicianService = clinicianService

        // RA: This strange stuff is needed because when a controller renders a template, the model property for the test is not assigned...
        def originalMethod = QuestionnaireEditorController.metaClass.getMetaMethod('render', [Map] as Class[])
        controller.metaClass.render = { Map args ->
            templateModel = args.model
            originalMethod.invoke(delegate, args)
        }
    }

    void "can get errors from invalid edited questionnaire"() {
        given:
        setupInvalidInput()
        request.method = 'POST'

        when:
        controller.save()

        then:
        response.status == 422
        def body = new JSONObject(templateModel)
        body.errors.errorCount == 2
    }

    void "can create validate and save valid edited questionnaire"() {
        given:
        clinicianService.currentClinician >> null
        setupValidInput()
        request.method = 'POST'

        when:
        controller.save()

        then:
        response.status == 200
    }

    def "when editing new questionnaire times of day is defaulted to 10"() {
        given:
        def header = QuestionnaireHeader.build(id: 1234)
        header.save(failOnError: true)

        when:
        def properties = controller.edit(1234, new QuestionnaireEditorCommand())

        then:
        properties.timesOfDay.size() == 1
        properties.timesOfDay[0].hour == 10
        properties.timesOfDay[0].minute == 0
    }

    def "when editing questionnaire with times of day from standard schedule set-up, it will use the one from standard schedule"() {
        given:
        def header = QuestionnaireHeader.build(id: 1234)
        def schedule = new StandardSchedule(internalTimesOfDay: "11:42")
        header.draftQuestionnaire = Questionnaire.build(questionnaireHeader: header, standardSchedule: schedule)
        header.save(failOnError: true)

        when:
        def properties = controller.edit(1234, new QuestionnaireEditorCommand())

        then:
        properties.timesOfDay.size() == 1
        properties.timesOfDay[0].hour == 11
        properties.timesOfDay[0].minute == 42
    }

    private def setupInvalidInput() {
        setupCommonInput()
        params.nodes = new JSONObject('''{
            "start1": {
                "type": "start",
                "id": "start1",
                "position": {
                    "top": 20,
                    "left": 20
                }
            }
        }''')
        params.connections = new JSONArray()
    }

    private def setupValidInput() {
        setupCommonInput()
        params.nodes = new JSONObject('''{
            "start1": {
                "type": "start",
                "id": "start1",
                "position": {
                    "top": 20,
                    "left": 20
                }
            },
            "end2": {
                "type": "end",
                "id": "end2",
                "position": {
                    "top": 274,
                    "left": 53
                }
            },
            "text3": {
                "type": "text",
                "headline": "Heading",
                "text": "Text",
                "id": "text3",
                "position": {
                    "top": 150,
                    "left": 50
                }
            }
        }''')
        params.connections = new JSONArray('''[
            {
                "source": "start1",
                "target": "text3",
                "severity": "",
                "type": "normal"
            },
            {
                "source": "text3",
                "target": "end2",
                "severity": "",
                "type": "normal"
            }
        ]''')
    }

    private def setupCommonInput() {
        params.title = "Measure Me"
        params.standardSchedule = new JSONObject('''{
            "type": "UNSCHEDULED",
            "reminderStartMinutes": "30",
            "introPeriodWeeks": "4",
            "dayInterval": "2",
            "startingDate": "05-12-2014",
            "specificDate": "05-12-2014",
            "timesOfDay": [],
            "reminderTime": {
                "hour": "10",
                "minute": "00"
            },
            "blueAlarmTime": {
                "hour": "23",
                "minute": "59"
            }
        }''')
        params['questionnaireHeader.id'] = "49"
    }
}
