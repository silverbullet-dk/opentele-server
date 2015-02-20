package org.opentele.server.provider.questionnaire
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.core.model.types.Weekday
import org.opentele.server.provider.command.QuestionnaireGroup2QuestionnaireHeaderCommand
import spock.lang.Specification

@TestFor(QuestionnaireGroupService)
@TestMixin(ControllerUnitTestMixin)
@Build([QuestionnaireHeader, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader])
class QuestionnaireGroupServiceSpec extends Specification {
    List<QuestionnaireHeader> questionnaireHeaders
    QuestionnaireGroup questionnaireGroup
    QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2QuestionnaireHeader

    def setup() {
        questionnaireHeaders = (1..5).collect {
            QuestionnaireHeader.build(name: "QH$it")
        }
        questionnaireGroup = QuestionnaireGroup.build()
        questionnaireGroup2QuestionnaireHeader = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: questionnaireHeaders.first(), questionnaireGroup: questionnaireGroup)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test that getUnusedQuestionnaireHeadersForQuestionnaireGroup returns the right questionnaire headers"() {
        when:
        def list = service.getUnusedQuestionnaireHeadersForQuestionnaireGroup(questionnaireGroup)

        then:
        list.size() == 4
        !list.contains(questionnaireHeaders.first())
    }

    def "when save is called with a valid command, a new object is created on the scheduleGroup"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup = questionnaireGroup
        command.selectedQuestionnaireHeader = questionnaireHeaders.last()

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1

        when:
        service.save(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 2
        questionnaireGroup.questionnaireGroup2Header*.questionnaireHeader.any { it == questionnaireHeaders.last() }
    }

    def "when save is called with a valid command where type is inherited (blank), the new group2header has no standardSchedule"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup = questionnaireGroup
        command.selectedQuestionnaireHeader = questionnaireHeaders.last()
        command.type = null

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1

        when:
        service.save(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 2
        !questionnaireGroup.questionnaireGroup2Header.find { it.questionnaireHeader == questionnaireHeaders.last() }.standardSchedule
    }

    def "when save is called with an invalid command, nothing is saved and a validation error is returned"() {
        given:
        def command = Mock(QuestionnaireGroup2QuestionnaireHeaderCommand)
        command.validate() >> false

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1

        when:
        service.save(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        0 * command.bindScheduleData(_ as Schedule)
    }

    def "when update is called with a valid command the questionnaireGroup2Header is updated correctly"() {
        given:
        questionnaireGroup2QuestionnaireHeader.standardSchedule = new StandardSchedule()

        and:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2QuestionnaireHeader
        command.selectedQuestionnaireHeader = questionnaireHeaders.last()

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.first()
        questionnaireGroup2QuestionnaireHeader.standardSchedule
        questionnaireGroup2QuestionnaireHeader.standardSchedule.type == Schedule.ScheduleType.UNSCHEDULED

        when:
        service.update(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.standardSchedule
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.last()
        questionnaireGroup2QuestionnaireHeader.standardSchedule.type == Schedule.ScheduleType.MONTHLY
     }

    def "when update is called where type is inherited (blank), the updated questionnaireGroup2header has no standardSchedule"() {
        given:
        questionnaireGroup2QuestionnaireHeader.standardSchedule = new StandardSchedule()

        and:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2QuestionnaireHeader
        command.selectedQuestionnaireHeader = questionnaireHeaders.last()
        command.type = null

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.first()
        questionnaireGroup2QuestionnaireHeader.standardSchedule

        when:
        service.update(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        !questionnaireGroup2QuestionnaireHeader.standardSchedule
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.last()
    }

    def "when update is called where type is set and the questionnaireGroup2Header has no standardSchedule a new schedule is created"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2QuestionnaireHeader
        command.selectedQuestionnaireHeader = questionnaireHeaders.last()

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2QuestionnaireHeader.standardSchedule

        when:
        service.update(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.standardSchedule
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.last()
        questionnaireGroup2QuestionnaireHeader.standardSchedule.type == Schedule.ScheduleType.MONTHLY
    }

    def "when update is called with an invalid command nothing is updated"() {
        given:
        def command = Mock(QuestionnaireGroup2QuestionnaireHeaderCommand)
        command.validate() >> false

        expect:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2QuestionnaireHeader.standardSchedule

        when:
        service.update(command)

        then:
        questionnaireGroup.questionnaireGroup2Header.size() == 1
        !questionnaireGroup2QuestionnaireHeader.standardSchedule
        questionnaireGroup2QuestionnaireHeader.questionnaireHeader == questionnaireHeaders.first()
        0 * command.bindScheduleData(_ as Schedule)
    }

    private getValidCommandObject() {
        new QuestionnaireGroup2QuestionnaireHeaderCommand(questionnaireGroupService: service, questionnaireGroup: new QuestionnaireGroup(),
                version: 0, type: Schedule.ScheduleType.MONTHLY,
                timesOfDay: [new Schedule.TimeOfDay(hour: 10, minute: 5), new Schedule.TimeOfDay(hour: 12, minute: 0)],
                daysInMonth: [1, 28], reminderStartMinutes: 60, weekdays: [Weekday.MONDAY, Weekday.FRIDAY], dayInterval: 5,
                startingDate: new Date().clearTime(), specificDate: new Date().clearTime() + 1)
    }

}
