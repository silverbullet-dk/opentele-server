package org.opentele.server.provider

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.MonitoringPlanBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.builders.QuestionnaireScheduleBuilder
import org.opentele.server.core.QuestionnaireService
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.core.model.types.Weekday
import org.opentele.server.provider.questionnaire.QuestionnaireProviderService
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(QuestionnaireProviderService)
@Build([QuestionnaireSchedule, PatientQuestionnaire, PatientBooleanNode, BooleanNode, ScheduleWindow])
@Mock(CompletedQuestionnaire)
class ScheduleWindowSpec extends Specification {

    QuestionnaireProviderService service
    Patient patient
    CompletedQuestionnaire completedQuestionnaire
    List<Schedule.TimeOfDay> timesOfDay

    def setup() {
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.WEEKDAYS, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.MONTHLY, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.EVERY_NTH_DAY, windowSizeMinutes: 30)

        service = new QuestionnaireProviderService()
        service.questionnaireService = new QuestionnaireService()
        patient = new PatientBuilder().build()

        def receivedDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:29:12").toCalendar()
        completedQuestionnaire = new CompletedQuestionnaireBuilder(receivedDate: receivedDate.getTime()).forPatient(patient).build()
        timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
    }

    @Unroll
    def "Test that the window size can be adjusted."() {
        when:
        def checkWindow = ScheduleWindow.findByScheduleType(scheduleType)
        if (checkWindow != null) {
            checkWindow.windowSizeMinutes = windowSizeMinutes
            checkWindow.save(flush: true)
        }

        Date startDate = Date.parse("yyyy/M/d", "2013/6/5")
        MonitoringPlan monitoringPlan = new MonitoringPlanBuilder().forPatient(patient).forStartDate(startDate).build();
        Date nthDayStartingDate = Date.parse("yyyy/M/d","2013/6/5")
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader)
                .forMonitoringPlan(monitoringPlan)
                .forScheduleType(scheduleType).forDaysInMonth([5])
                .forStartingDate(nthDayStartingDate).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.any() == hasBlueAlarm

        where:

        scheduleType                        | windowSizeMinutes | hasBlueAlarm
        Schedule.ScheduleType.WEEKDAYS      | 30                | true
        Schedule.ScheduleType.WEEKDAYS      | 35                | false
        Schedule.ScheduleType.EVERY_NTH_DAY | 30                | true
        Schedule.ScheduleType.EVERY_NTH_DAY | 35                | false
        Schedule.ScheduleType.MONTHLY       | 30                | true
        Schedule.ScheduleType.MONTHLY       | 35                | false
        Schedule.ScheduleType.UNSCHEDULED   | 30                | false
        Schedule.ScheduleType.UNSCHEDULED   | 35                | false
    }

    Calendar subtractOneMinute(Calendar c) {
        Calendar result = c.clone() as Calendar
        result.add(Calendar.MINUTE, -1)
        result
    }
}
