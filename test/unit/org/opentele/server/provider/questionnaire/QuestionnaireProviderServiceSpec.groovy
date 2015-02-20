package org.opentele.server.provider.questionnaire
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.MonitoringPlanBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.builders.QuestionnaireScheduleBuilder
import org.opentele.server.core.QuestionnaireService
import org.opentele.server.core.command.AddQuestionnaireGroup2MonitoringPlanCommand
import org.opentele.server.core.command.QuestionnaireGroup2HeaderCommand
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.*
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Severity
import org.opentele.server.core.model.types.Unit
import org.opentele.server.core.model.types.Weekday
import org.opentele.server.provider.Exception.DeleteQuestionnaireException
import org.opentele.server.core.QuestionnaireNodeService
import org.opentele.taglib.FormattingTagLib
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

import static org.opentele.server.core.model.Schedule.ScheduleType.*
import static org.opentele.server.core.model.types.Weekday.FRIDAY
import static org.opentele.server.core.model.types.Weekday.MONDAY

@TestFor(QuestionnaireProviderService)
@Build([StandardSchedule, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader, QuestionnaireHeader, Questionnaire, QuestionnaireNode, QuestionnaireSchedule, PatientBooleanNode, BooleanNode, ScheduleWindow, PatientQuestionnaire, MonitoringPlan, CompletedQuestionnaire])
@Mock([CompletedQuestionnaire, Questionnaire2MeterType])
class QuestionnaireProviderServiceSpec extends Specification {
    Patient patient
    Date monitoringPlanStartDate
    MonitoringPlan monitoringPlan

    def setup() {
        patient = new PatientBuilder().build()
        monitoringPlanStartDate = Date.parse("yyyy/M/d", "2013/6/5")
        monitoringPlan = new MonitoringPlanBuilder().forPatient(patient).forStartDate(monitoringPlanStartDate).build()
        //Set windowsize that match old static windowsizes
        ScheduleWindow.build(scheduleType: WEEKDAYS, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: MONTHLY, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: EVERY_NTH_DAY, windowSizeMinutes: 30)

        service.questionnaireNodeService = Mock(QuestionnaireNodeService)
        service.questionnaireService = new QuestionnaireService()
    }

    def 'triggers alarm if there is no completed questionnaire'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.size() == 1
    }

    def 'triggers no blue alarm before deadline'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:59:00").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm before starting date of monitoring plan for weekday schedule'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/4 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm before starting date of monitoring plan for monthly schedule'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(MONTHLY).forDaysInMonth([4, 5]).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/4 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm before starting date of monitoring plan for nth-day schedule'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        def nthDayStartingDate = Date.parse("yyyy/M/d","2013/6/4")
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan)
                .forScheduleType(EVERY_NTH_DAY).forIntervalInDays(2)
                .forStartingDate(nthDayStartingDate).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/4 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm before starting date of monitoring plan for specific-day schedule'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        def specificDate = Date.parse("yyyy/M/d", "2013/6/4")
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(SPECIFIC_DATE).forSpecificDate(specificDate).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/4 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm when questionnaire is uploaded on time'() {
        setup:
        def uploadDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:53:12").toCalendar()
        def completedQuestionnaire = new CompletedQuestionnaireBuilder(uploadDate: uploadDate.getTime()).forPatient(patient).build()
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader).forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers blue alarm alarm when questionnaire is uploaded too early'() {
        def receivedDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:13:12").toCalendar()
        def completedQuestionnaire = new CompletedQuestionnaireBuilder(receivedDate: receivedDate.getTime()).forPatient(patient).build()
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader).forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.size() == 1
    }

    def 'triggers no blue alarm when scheduled at two times a day and the first time has no uploaded questionnaire'() {
        setup:
        def receivedDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:54:12")
        def completedQuestionnaire = new CompletedQuestionnaireBuilder(receivedDate: receivedDate).forPatient(patient).build()
        def timesOfDay = [new Schedule.TimeOfDay(hour: 15, minute: 50), new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader).forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers blue alarm when scheduled at two times a day and nothing is uploaded at the second time'() {
        setup:
        def receivedDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:44:12")
        def completedQuestionnaire = new CompletedQuestionnaireBuilder(receivedDate: receivedDate).forPatient(patient).build()
        def timesOfDay = [new Schedule.TimeOfDay(hour: 15, minute: 50), new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader).forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.size() == 1
    }

    def 'will not trigger blue alarms for dismissed patients'() {
        setup:
        patient = new PatientBuilder().forState(PatientState.DISCHARGED_EQUIPMENT_DELIVERED).build()
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    def 'triggers no blue alarm when questionnaire is unpublished'() {
        setup:
        def timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
        def qs = new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan).forScheduleType(WEEKDAYS).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()
        qs.questionnaireHeader.activeQuestionnaire = null  // unpublish active questionnaire

        when:
        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.empty
    }

    @Ignore("Questionnaire.executeUpdate does not work in mock GORM")
    def "removing a draft questionnaire will succede"() {
        setup:
        def questionnaireHeader = QuestionnaireHeader.build()
        def questionnaire = Questionnaire.build(questionnaireHeader: questionnaireHeader)
        questionnaireHeader.draftQuestionnaire = questionnaire
        def questionnaireId = questionnaire.id

        when:
        service.deleteQuestionnaire(questionnaire)
        and:
        questionnaire.save(flush: true) // save to memory

        then:
        1 * service.questionnaireNodeService.deleteQuestionnaireNodes(questionnaire, true)
        !Questionnaire.get(questionnaireId)
        !questionnaireHeader.draftQuestionnaire
    }

    @Ignore("Questionnaire.executeUpdate does not work in mock GORM")
    def "removing a used questionnaire will fail"() {
        setup:
        def questionnaireHeader = QuestionnaireHeader.build()
        def questionnaire = Questionnaire.build(
                questionnaireHeader: questionnaireHeader,
                patientQuestionnaires: [new PatientQuestionnaire()]
        )
        questionnaireHeader.draftQuestionnaire = questionnaire

        when:
        service.deleteQuestionnaire(questionnaire)


        then:
        thrown(DeleteQuestionnaireException)
    }

    @Unroll
    def "test we get the correct icon for a measurement"() {
        setup:
        def g = Mock(FormattingTagLib)
        patient.blueAlarmQuestionnaireIDs = blueAlarms

        when:
        def res = service.iconAndTooltip(g, patient, [CompletedQuestionnaire.build(severity: severity)])

        then:
        res[0] == icon

        where:
        severity                 | icon                            | blueAlarms
        Severity.NONE            | Severity.NONE.icon()            | []
        Severity.GREEN           | Severity.GREEN.icon()           | []
        Severity.BLUE            | Severity.BLUE.icon()            | ['1']
        Severity.YELLOW          | Severity.YELLOW.icon()          | []
        Severity.RED             | Severity.RED.icon()             | []
        Severity.ABOVE_THRESHOLD | Severity.ABOVE_THRESHOLD.icon() | []
        Severity.BELOW_THRESHOLD | Severity.BELOW_THRESHOLD.icon() | []
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that addOrUpdateQuestionnairesOnMonitoringPlan works as expected"() {
        setup:
        def standardSchedule = new StandardSchedule()
        standardSchedule.dayInterval = standardScheduleVal
        def questionnaireSchedule = new QuestionnaireSchedule()
        questionnaireSchedule.dayInterval = questionnaireScheduleVal

        when:
        def result = service.isEveryNthDayEquals(standardSchedule, questionnaireSchedule)

        then:
        result == expected

        where:
        standardScheduleVal | questionnaireScheduleVal | expected
        1                   | 1                        | true
        1                   | 2                        | false
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that isDaysInMonthEquals returns the correct results"() {
        setup:
        def standardSchedule = new StandardSchedule(internalDaysInMonth: standardScheduleVal)
        def questionnaireSchedule = new QuestionnaireSchedule(internalDaysInMonth: questionnaireScheduleVal)

        expect:
        exptected == service.isDaysInMonthEquals(standardSchedule, questionnaireSchedule)

        where:
        standardScheduleVal | questionnaireScheduleVal | exptected
        "1,10,20"           | "1,10,20"                | true
        "1,10,20"           | "10,20,1"                | true
        "1,2,3"             | "2,3,5"                  | false
        "1,10,20"           | "1,10,20,21"             | false
        "1,10,20,21"        | "1,10,20"                | false
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that isTimesOfDayEquals returns the correct results"() {
        setup:
        def standardSchedule = new StandardSchedule(internalTimesOfDay: standardScheduleVal)
        def questionnaireSchedule = new QuestionnaireSchedule(internalTimesOfDay: questionnaireScheduleVal)

        expect:
        exptected == service.isTimesOfDayEquals(standardSchedule, questionnaireSchedule)

        where:
        standardScheduleVal | questionnaireScheduleVal | exptected
        "10:00"             | "10:00"                  | true
        "10:00,12:00"       | "10:00,12:00"            | true
        "10:00,12:00"       | "12:00,10:00"            | true
        "16:00,10:00,12:00" | "12:00,10:00,16:00"      | true
        "10:00"             | "12:00"                  | false
        "10:00"             | "12:00,10:00"            | false
        "10:00,12:00"       | "12:00"                  | false
        "10:00,12:00"       | "12:00,15:00"            | false
        "16:00,10:00,12:00" | "12:00,10:00,17:00"      | false
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that isWeekdaysEquals returns the correct results"() {
        setup:
        def standardSchedule = new StandardSchedule(internalWeekdays: standardScheduleVal)
        def questionnaireSchedule = new QuestionnaireSchedule(internalWeekdays: questionnaireScheduleVal)

        expect:
        exptected == service.isTimesOfDayEquals(standardSchedule, questionnaireSchedule)

        where:
        standardScheduleVal     | questionnaireScheduleVal | exptected
        "MONDAY"                | "MONDAY"                 | true
        "MONDAY,TUESDAY"        | "MONDAY,TUESDAY"         | true
        "FRIDAY,MONDAY,TUESDAY" | "MONDAY,TUESDAY,FRIDAY"  | true
        "MONDAY"                | "TUESDAY"                | true
        "MONDAY"                | "MONDAY,TUESDAY"         | true
        "MONDAY,TUESDAY"        | "TUESDAY"                | true
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that isTypeEquals returns the correct results"() {
        setup:
        def args = [internalTimesOfDay: "10:00", internalDaysInMonth: "1,2", internalWeekdays: "MONDAY"]
        def standardSchedule = new StandardSchedule(args)
        def questionnaireSchedule = new QuestionnaireSchedule(args)
        standardSchedule.type = standardScheduleType
        questionnaireSchedule.type = questionnaireScheduleType

        expect:
        exptected == service.isSchedulesEqual(standardschedulenull ? null : standardSchedule, questionnaireSchedule)

        where:
        standardScheduleType | questionnaireScheduleType | standardschedulenull | exptected
        UNSCHEDULED          | UNSCHEDULED               | false                | true
        null                 | UNSCHEDULED               | true                 | true
        EVERY_NTH_DAY        | WEEKDAYS                  | false                | false
        WEEKDAYS             | WEEKDAYS                  | false                | true
        MONTHLY              | MONTHLY                   | false                | true
        EVERY_NTH_DAY        | EVERY_NTH_DAY             | false                | true
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that isSchedulesEquals returns the correct results"() {
        setup:

        def standardSchedule = new StandardSchedule(type: standardScheduleVal)
        def questionnaireSchedule = new QuestionnaireSchedule(type: questionnaireScheduleVal)

        expect:
        exptected == service.isTypeEquals(standardSchedule, questionnaireSchedule)

        where:
        standardScheduleVal | questionnaireScheduleVal | exptected
        UNSCHEDULED         | UNSCHEDULED              | true
        UNSCHEDULED         | WEEKDAYS                 | false
        EVERY_NTH_DAY       | WEEKDAYS                 | false
    }


    // TODO

//    @SuppressWarnings("GroovyAccessibility")
//    @Unroll
//    def "test MeasurementDescription.orderedUnits() formats units correctly"() {
//        setup:
//        def measurementDescription = new MeasurementDescription(units: testUnitSet)
//
//        expect:
//        expected == measurementDescription.orderedUnits()
//
//        where:
//        testUnitSet                             | expected
//        null                                    | []
//        [Unit.BPM, Unit.PERCENTAGE] as Set      | [Unit.PERCENTAGE, Unit.BPM]
//        [Unit.PERCENTAGE, Unit.BPM] as Set      | [Unit.PERCENTAGE, Unit.BPM]
//        [Unit.MMHG, Unit.BPM] as Set            | [Unit.MMHG, Unit.BPM]
//        [Unit.BPM, Unit.MMHG] as Set            | [Unit.MMHG, Unit.BPM]
//        [Unit.KILO, Unit.MMHG] as Set           | [Unit.KILO, Unit.MMHG]
//        [Unit.MMHG] as Set                      | [Unit.MMHG]
//        [Unit.BPM, Unit.BPM, Unit.BPM] as Set   | [Unit.BPM]
//        [Unit.BPM, Unit.KILO, Unit.MMHG] as Set | [Unit.BPM, Unit.KILO, Unit.MMHG]
//    }

    @SuppressWarnings("GroovyAccessibility")
    def "test findExistingQuestionnaireSchedule returns questionnaireSchedule match is found"() {
        setup:
        def questionnaireHeader = QuestionnaireHeader.build()
        def questionnaireSchedules = (1..10).collect { QuestionnaireSchedule.build(questionnaireHeader: QuestionnaireHeader.build()) }
        def questionnaireSchedule = questionnaireSchedules[5]
        questionnaireSchedule.questionnaireHeader = questionnaireHeader

        expect:
        questionnaireSchedule == service.findExistingQuestionnaireSchedule(questionnaireSchedules, questionnaireHeader)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test findExistingQuestionnaireSchedule returns null if no questionnaireSchedule match is found"() {
        setup:
        def questionnaireHeader = QuestionnaireHeader.build()
        def questionnaireSchedules = (1..10).collect { QuestionnaireSchedule.build(questionnaireHeader: QuestionnaireHeader.build()) }

        expect:
        !service.findExistingQuestionnaireSchedule(questionnaireSchedules, questionnaireHeader)
    }

    def "test findQuestionnaireHeadersAndOverlapWithExistingQuestionnaireSchedules"() {
        setup:
        // Make 5 questionnaire headers and add them to a QuestionnaireGroup
        def questionnaireGroup = QuestionnaireGroup.build()
        def questionnaireHeaders = (1..5).collect {
            def questionnaire = Questionnaire.build(standardSchedule: StandardSchedule.build())
            def questionnaireHeader = QuestionnaireHeader.build(activeQuestionnaire: questionnaire, questionnaires: [questionnaire])
            questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireHeader: questionnaireHeader)
            return questionnaireHeader
        }
        // Change the one questionnaireGroup2QuestionnaireHeader to have a specific schedule
        questionnaireGroup.questionnaireGroup2Header.find {
            it.questionnaireHeader == questionnaireHeaders[1]
        }.standardSchedule = StandardSchedule.build(type: Schedule.ScheduleType.WEEKDAYS, internalWeekdays: "MONDAY", internalTimesOfDay: "10:00")

        // Make 10 questionnaireSchedules for a monitoringPlan and make two of them have the same questionnaireHeader as the Group
        def questionnaireSchedules = (1..10).collect { QuestionnaireSchedule.build(questionnaireHeader: QuestionnaireHeader.build()) }
        questionnaireSchedules[5].questionnaireHeader = questionnaireHeaders[0]
        questionnaireSchedules[6].questionnaireHeader = questionnaireHeaders[1]

        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: questionnaireSchedules)

        when:
        def candidates = service.findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(questionnaireGroup, monitoringPlan)

        then:
        candidates.size() == 5
        candidates[0].questionnaireSchedule == questionnaireSchedules[5]
        !candidates[0].questionnaireScheduleOverlap
        candidates[1].questionnaireSchedule == questionnaireSchedules[6]
        candidates[1].questionnaireScheduleOverlap
        !candidates[2].questionnaireSchedule
        !candidates[3].questionnaireSchedule
        !candidates[4].questionnaireSchedule
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test that findQuestionnaireInMonitoringPlan works when questionnaireHeader is found"() {
        setup:
        def questionnaire = Questionnaire.build(standardSchedule: StandardSchedule.build())
        def questionnaireHeader = QuestionnaireHeader.build(activeQuestionnaire: questionnaire, questionnaires: [questionnaire])
        def questionnaireGroup2Header = new QuestionnaireGroup2QuestionnaireHeader(questionnaireHeader: questionnaireHeader)
        def questionnaireGroup2HeaderCmd = new QuestionnaireGroup2HeaderCommand(questionnaireGroup2Header: questionnaireGroup2Header)

        // Make 10 questionnaireSchedules for a monitoringPlan and make on of them have the same questionnaireHeader as the Group
        def questionnaireSchedules = (1..10).collect { QuestionnaireSchedule.build(questionnaireHeader: QuestionnaireHeader.build()) }
        questionnaireSchedules[6].questionnaireHeader = questionnaireHeader

        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: questionnaireSchedules)

        expect:
        service.findQuestionnaireInMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test that findQuestionnaireInMonitoringPlan works when questionnaireHeader is not found"() {
        setup:
        def questionnaire = Questionnaire.build(standardSchedule: StandardSchedule.build())
        def questionnaireHeader = QuestionnaireHeader.build(activeQuestionnaire: questionnaire, questionnaires: [questionnaire])
        def questionnaireGroup2Header = new QuestionnaireGroup2QuestionnaireHeader(questionnaireHeader: questionnaireHeader)
        def questionnaireGroup2HeaderCmd = new QuestionnaireGroup2HeaderCommand(questionnaireGroup2Header: questionnaireGroup2Header)

        // Make 10 questionnaireSchedules for a monitoringPlan, where none are in the group
        def questionnaireSchedules = (1..10).collect { QuestionnaireSchedule.build(questionnaireHeader: QuestionnaireHeader.build()) }

        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: questionnaireSchedules)

        expect:
        !service.findQuestionnaireInMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
    }

    def "test that findAddedQuestionnaireGroup2HeaderCommands only returns those selected and where the questionnaireHeader2Group is actually found "() {
        setup:
        def qg = QuestionnaireGroup.build()
        def q2h1 = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: QuestionnaireHeader.build(), questionnaireGroup: qg)
        def q2h2 = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: QuestionnaireHeader.build(), questionnaireGroup: qg)
        def command = new AddQuestionnaireGroup2MonitoringPlanCommand()
        command.questionnaireGroup2Headers[q2h1.id] = ['addQuestionnaire': true, 'useStandard': false]
        command.questionnaireGroup2Headers[q2h2.id] = ['addQuestionnaire': false, 'useStandard': false]
        command.questionnaireGroup2Headers[3] = ['addQuestionnaire': true, 'useStandard': false]
        println command.questionnaireGroup2Headers

        when:
        def filtered = service.findAddedQuestionnaireGroup2HeaderCommands(command)

        then:
        filtered.size() == 1
        q2h1 in filtered*.questionnaireGroup2Header
    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that a StandardSchedule can be assigned to a QuestionnaireSchedule"() {
        setup:
        def questionnaireSchedule = new QuestionnaireSchedule(type: UNSCHEDULED, questionnaireHeader: QuestionnaireHeader.build(), monitoringPlan: MonitoringPlan.build())
        def standardSchedule = new StandardSchedule(type: type, internalWeekdays: internalWeekdays,
                internalDaysInMonth: internalDaysInMonth, dayInterval: dayInterval,
                internalTimesOfDay: "10:00,12:00", reminderStartMinutes: 60)
        standardSchedule.dayInterval = dayInterval

        expect:
        !questionnaireSchedule.internalWeekdays
        !questionnaireSchedule.internalTimesOfDay

        when:
        service.assignStandardScheduleToQuestionnaireSchedule(standardSchedule, questionnaireSchedule)

        then:
        questionnaireSchedule.type == expectedType
        questionnaireSchedule.weekdays == expectedWeekdays
        questionnaireSchedule.timesOfDay == [Schedule.TimeOfDay.toTimeOfDay(10, 0), Schedule.TimeOfDay.toTimeOfDay(12, 0)]
        questionnaireSchedule.dayInterval == dayInterval
        questionnaireSchedule.reminderStartMinutes == 60

        where:
        type          | internalWeekdays | internalDaysInMonth | expectedType  | expectedWeekdays | expectedDaysInMonth | dayInterval
        WEEKDAYS      | "MONDAY,FRIDAY"  | ''                  | WEEKDAYS      | [MONDAY, FRIDAY] | []                  | 2
        MONTHLY       | ''               | '1,2,6,7'           | MONTHLY       | []               | [1, 2, 6, 7]        | 2
        EVERY_NTH_DAY | ''               | ''                  | EVERY_NTH_DAY | []               | []                  | 5

    }

    @SuppressWarnings("GroovyAccessibility")
    @Unroll
    def "test that updateQuestionnaireSchedule will be updated from the QuestionnaireGroup2HeaderCommand"() {
        setup:
        def questionnaireSchedule = QuestionnaireSchedule.build(type: UNSCHEDULED)
        def standardSchedule = new StandardSchedule(type: type, internalWeekdays: "MONDAY",
                internalTimesOfDay: "10:00,12:00", reminderStartMinutes: 60)
        def command = new QuestionnaireGroup2HeaderCommand(useStandard: useStandard, questionnaireGroup2Header: new QuestionnaireGroup2QuestionnaireHeader(standardSchedule: standardSchedule))

        when:
        service.updateQuestionnaireSchedule(command, questionnaireSchedule)

        then:
        questionnaireSchedule.type == expectedType

        where:
        type        | useStandard | expectedType
        WEEKDAYS    | false       | UNSCHEDULED
        WEEKDAYS    | true        | WEEKDAYS
        UNSCHEDULED | true        | UNSCHEDULED
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test that addQuestionnaireToMonitoringPlan adds correctly from a QuestionnaoreGroup2HeaderCommand"() {
        setup:
        def monitoringPlan = MonitoringPlan.build()
        def standardSchedule = new StandardSchedule(type: WEEKDAYS, internalWeekdays: "MONDAY",
                internalTimesOfDay: "10:00,12:00", reminderStartMinutes: 60)
        def command = new QuestionnaireGroup2HeaderCommand(questionnaireGroup2Header: new QuestionnaireGroup2QuestionnaireHeader(standardSchedule: standardSchedule, questionnaireHeader: QuestionnaireHeader.build()))

        expect:
        !monitoringPlan.questionnaireSchedules

        when:
        service.addQuestionnaireToMonitoringPlan(command, monitoringPlan)

        then:
        monitoringPlan.questionnaireSchedules.size() == 1
    }

    def "test that addOrUpdateQuestionnairesOnMonitoringPlan works"() {
        setup:
        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: [QuestionnaireSchedule.build(type: UNSCHEDULED)])
        def standardSchedule = new StandardSchedule(type: WEEKDAYS, internalWeekdays: "MONDAY",
                internalTimesOfDay: "10:00,12:00", reminderStartMinutes: 60)
        def questionnaireGroup = QuestionnaireGroup.build()
        def q2h1 = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: monitoringPlan.questionnaireSchedules.find().questionnaireHeader, standardSchedule: standardSchedule, questionnaireGroup: questionnaireGroup)
        def questionnaire = Questionnaire.build(standardSchedule: new StandardSchedule(type: MONTHLY, internalDaysInMonth: "1,2,5,6,10,11", internalTimesOfDay: "10:00"))
        def q2h2 = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: QuestionnaireHeader.build(activeQuestionnaire: questionnaire, questionnaires: [questionnaire]), questionnaireGroup: questionnaireGroup)
        def q2h3 = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: QuestionnaireHeader.build(),questionnaireGroup: questionnaireGroup)
        def command = new AddQuestionnaireGroup2MonitoringPlanCommand(monitoringPlan: monitoringPlan)
        command.questionnaireGroup2Headers[q2h1.id] = ['addQuestionnaire': true, 'useStandard': true]
        command.questionnaireGroup2Headers[q2h2.id] = ['addQuestionnaire': true, 'useStandard': false]
        command.questionnaireGroup2Headers[q2h3.id] = ['addQuestionnaire': true, 'useStandard': false]

        expect:
        monitoringPlan.questionnaireSchedules.size() == 1

        when:
        service.addOrUpdateQuestionnairesOnMonitoringPlan(command)

        then:
        monitoringPlan.questionnaireSchedules.size() == 3
        command.addedQuestionnaires.size() == 2
        command.updatedQuestionnaires.size() == 1
    }

    Calendar subtractOneMinute(Calendar c) {
        def result = c.clone()
        result.add(Calendar.MINUTE, -1)
        result
    }
}
