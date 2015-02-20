package org.opentele.builders
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.Patient
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.Schedule.TimeOfDay
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.core.model.types.Weekday

class QuestionnaireScheduleBuilder {


    private Schedule.ScheduleType scheduleType = Schedule.ScheduleType.UNSCHEDULED
    private internalWeekdays = ''
    private internalDaysInMonth = ''
    private internalWeekdaysIntroPeriod = ''
    private internalWeekdaysSecondPeriod = ''
    private internalReminderTime = '10:00'
    private internalBlueAlarmTime = '23:59'
    private int introPeriodWeeks = 4
    private startingDate = today()
    private dayInterval = 2
    private specificDate = null
    private timesOfDay = [new Schedule.TimeOfDay(hour: 12, minute: 30)]
    private Patient patient = (new PatientBuilder()).build()
    private MonitoringPlan monitoringPlan
    private QuestionnaireHeader questionnaireHeader
    private String questionnaireName
    private int reminderStartMinutes = 30

    QuestionnaireScheduleBuilder forMonitoringPlan(MonitoringPlan monitoringPlan) {
        this.monitoringPlan = monitoringPlan
        this
    }

    QuestionnaireScheduleBuilder forTimesOfDay(List<Schedule.TimeOfDay> timesOfDays) {
        this.timesOfDay = timesOfDays;
        this
    }

    QuestionnaireScheduleBuilder forScheduleType(Schedule.ScheduleType scheduleType) {
        this.scheduleType = scheduleType

        this
    }

    QuestionnaireScheduleBuilder forPatient(Patient patient) {
        this.patient = patient

        this
    }

    QuestionnaireScheduleBuilder forWeekdays(List<Weekday> weekdays) {
        def schedule = new QuestionnaireSchedule()
        schedule.setWeekdays(weekdays)
        this.internalWeekdays = schedule.internalWeekdays

        this
    }
    QuestionnaireScheduleBuilder forWeekdaysOnce(List<Weekday> weekdaysIntroPeriod, List<Weekday> weekdaysSecondPeriod) {
        def schedule = new QuestionnaireSchedule()
        schedule.setWeekdaysIntroPeriod(weekdaysIntroPeriod)
        schedule.setWeekdaysSecondPeriod(weekdaysSecondPeriod)
        this.internalWeekdaysIntroPeriod = schedule.internalWeekdaysIntroPeriod
        this.internalWeekdaysSecondPeriod = schedule.internalWeekdaysSecondPeriod
        this
    }
    
    QuestionnaireScheduleBuilder forIntroPeriodWeeks(int introPeriodWeeks) {
        this.introPeriodWeeks = introPeriodWeeks
        this
    }

    QuestionnaireScheduleBuilder forBlueAlarmTime(TimeOfDay blueAlarmTime) {
        this.internalBlueAlarmTime = blueAlarmTime.toString()
        this
    }

    QuestionnaireScheduleBuilder forReminderTime(TimeOfDay reminderTime) {
        this.internalReminderTime = reminderTime.toString()
        this
    }

    QuestionnaireScheduleBuilder forStartingDate(Date startingDate) {
        def schedule = new QuestionnaireSchedule()
        schedule.setStartingDate(startingDate)

        this.startingDate = schedule.startingDate

        this
    }

    QuestionnaireScheduleBuilder forIntervalInDays(int interval) {
        this.dayInterval = interval

        this
    }

    QuestionnaireScheduleBuilder forSpecificDate(Date specificDate) {
        this.specificDate = specificDate

        this
    }

    QuestionnaireScheduleBuilder forDaysInMonth(List<Integer> daysInMonth) {
        def schedule = new QuestionnaireSchedule()
        schedule.setDaysInMonth(daysInMonth)

        this.internalDaysInMonth = schedule.internalDaysInMonth

        this
    }

    QuestionnaireScheduleBuilder forReminderStartMinutes(int minutes) {
        this.reminderStartMinutes = reminderStartMinutes

        this
    }

    QuestionnaireScheduleBuilder forQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName

        this
    }

    QuestionnaireSchedule build() {
        def internalTimesOfDayGenerator = new QuestionnaireSchedule() //Get a QuestionnaireSchedule to build the 'internalTimesOfDay' string for us.
        internalTimesOfDayGenerator.setTimesOfDay(this.timesOfDay)
        def internalTimesOfDay = internalTimesOfDayGenerator.internalTimesOfDay

        // Make life a little easier by fetching patient from monitoring plan, if present
        if (patient == null && monitoringPlan != null) {
            patient = monitoringPlan.patient
        }

        if (monitoringPlan == null) {
            monitoringPlan = new MonitoringPlanBuilder().forPatient(patient).build()
        }

        if (questionnaireHeader == null) {
            QuestionnaireHeaderBuilder questionnaireHeaderBuilder = new QuestionnaireHeaderBuilder()
            if (questionnaireName != null) {
                questionnaireHeaderBuilder = questionnaireHeaderBuilder.forName(questionnaireName)
            }
            questionnaireHeader = questionnaireHeaderBuilder.build()
        }

        QuestionnaireSchedule schedule = QuestionnaireSchedule.build(
                monitoringPlan: monitoringPlan,
                questionnaireHeader: questionnaireHeader,
                internalTimesOfDay: internalTimesOfDay,
                internalWeekdays: this.internalWeekdays,
                internalDaysInMonth: this.internalDaysInMonth,
                startingDate: this.startingDate,
                specificDate: this.specificDate,
                reminderStartMinutes: this.reminderStartMinutes,
                internalWeekdaysIntroPeriod: this.internalWeekdaysIntroPeriod,
                internalWeekdaysSecondPeriod: this.internalWeekdaysSecondPeriod,
                introPeriodWeeks: this.introPeriodWeeks,
                internalReminderTime: this.internalReminderTime,
                internalBlueAlarmTime: this.internalBlueAlarmTime
        )

        schedule.type = this.scheduleType
        schedule.dayInterval = dayInterval

        schedule.save(failOnError: true)

        schedule
    }

    private Date today() {
        def result = Calendar.getInstance()
        result.set(Calendar.HOUR_OF_DAY, 0)
        result.set(Calendar.MINUTE, 0)
        result.set(Calendar.SECOND, 0)
        result.set(Calendar.MILLISECOND, 0)
        result.getTime()
    }

}
