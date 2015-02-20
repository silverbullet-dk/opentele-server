package org.opentele.server.provider
import grails.test.MockUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.provider.command.QuestionnaireGroup2QuestionnaireHeaderCommand
import org.opentele.server.core.test.CommandCanValidateSpecification
import org.opentele.server.provider.questionnaire.QuestionnaireGroupService
import spock.lang.Specification

import static org.opentele.server.core.model.Schedule.ScheduleType.MONTHLY
import static org.opentele.server.core.model.types.Weekday.FRIDAY
import static org.opentele.server.core.model.types.Weekday.MONDAY

@TestMixin(GrailsUnitTestMixin)
@Mock([QuestionnaireHeader, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader])
class QuestionnaireGroup2QuestionnaireHeaderCommandSpec extends CommandCanValidateSpecification {
    def setup() {
        MockUtils.prepareForConstraintsTests(QuestionnaireGroup2QuestionnaireHeaderCommand, [:])
        mockForConstraintsTests QuestionnaireGroup2QuestionnaireHeaderCommand
    }

    def "when a valid QuestionnaireGroup2QuestionnaireHeaderCommand is validated, everything runs smooth"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2QuestionnaireHeader

        expect:
        command.validate()

        where:
        questionnaireGroup2QuestionnaireHeader << [null, new QuestionnaireGroup2QuestionnaireHeader(version: 0)]
    }

    def "when the questionnaireGroup2QuestionnaireHeaderCommand.questionnaireGroup2QuestionnaireHeader version is ahead of the command version validation fails"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader.version = 2

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['questionnaireGroup2QuestionnaireHeader'] == 'optimistic.locking.failure'

    }

    def "when the questionnaireScheduleCommand.questionnaireGroup is not set validation fails"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup = null

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['questionnaireGroup'] == 'nullable'

    }

    private getValidCommandObject() {
        def service = new QuestionnaireGroupService()

        new QuestionnaireGroup2QuestionnaireHeaderCommand(selectedQuestionnaireHeader: new QuestionnaireHeader() , questionnaireGroupService: service, questionnaireGroup: new QuestionnaireGroup(),
                questionnaireGroup2QuestionnaireHeader: new QuestionnaireGroup2QuestionnaireHeader(version: 0),
                version: 0, type: MONTHLY,
                timesOfDay: [new Schedule.TimeOfDay(hour: 10, minute: 5), new Schedule.TimeOfDay(hour: 12, minute: 0)],
                daysInMonth: [1, 28], reminderStartMinutes: 60, weekdays: [MONDAY, FRIDAY], dayInterval: 5,
                startingDate: new Date().clearTime(), specificDate: new Date().clearTime() + 1)
    }
}
