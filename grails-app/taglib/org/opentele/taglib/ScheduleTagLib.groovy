package org.opentele.taglib
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.types.Weekday

import static org.opentele.server.core.model.Schedule.ScheduleType.*

class ScheduleTagLib {
	static namespace = "schedule"
    FormTagLib formTagLib = new FormTagLib()

	def humanReadableSchedule = { attrs ->
        Schedule schedule = attrs.remove('schedule')
        out << '<span '
        formTagLib.outputAttributes(attrs, out)
        out << '>'
        switch (schedule?.type) {
            case null:
                outputNoCurrentVersion()
                break
            case UNSCHEDULED:
                outputNoSchedule()
                break
            case MONTHLY:
                outputTime(schedule)
                outputMonthlySchedule(schedule)
                break
            case WEEKDAYS:
                outputTime(schedule)
                outputWeeklySchedule(schedule)
                break
            case WEEKDAYS_ONCE:
                outputWeeklyOnceSchedule(schedule)
                break
            case EVERY_NTH_DAY:
                outputTime(schedule)
                outputNthDaySchedule(schedule)
                break
            case SPECIFIC_DATE:
                outputTime(schedule)
                outputSpecificDateSchedule(schedule)
                break
            default:
                 throw new IllegalArgumentException("Unknown schedule type: '${schedule.type}'")
        }
        out << '</span>'
	}

    private def outputNthDaySchedule(Schedule schedule) {
        out << g.message(code: message(code:"schedule.nthdayschedule"), args: [schedule.dayInterval])
    }

    private def outputSpecificDateSchedule(Schedule schedule) {
        def date = g.formatDate(date: schedule.specificDate, format:  "dd/MM/yyyy")
        out << g.message(code: message(code:"schedule.specificdateschedule"), args: [date])
    }

    private void outputWeeklySchedule(Schedule schedule) {
        out << weekdaysToString(schedule.weekdays)
    }

    private void outputWeeklyOnceSchedule(Schedule schedule) {
        def introPeriod = weekdaysToString(schedule.weekdaysIntroPeriod)
        def secondPeriod = weekdaysToString(schedule.weekdaysSecondPeriod)

        out << g.message(code: 'schedule.introPeriod', args: [schedule.reminderTime, introPeriod, g.message(code: 'questionnaireSchedule.introPeriodWeeks.week.'+schedule.introPeriodWeeks), secondPeriod])
    }

    private void outputMonthlySchedule(Schedule schedule) {
        def days = schedule.daysInMonth.join('., ') + '.'
        out << g.message(code: 'schedule.monthlySchedule', args: [days])
    }

    private void outputNoSchedule() {
        out << g.message(code: 'schedule.scheduleType.UNSCHEDULED.label')
    }

    private void outputNoCurrentVersion() {
        out << g.message(code: 'questionnaireSchedule.noCurrentVersion.label')
    }

    private void outputTime(Schedule schedule) {
        out << schedule.timesOfDay.join(" ")+" "
    }

    private String weekdaysToString(List<Weekday> weekdays) {
        weekdays.collect { g.message(code: "enum.weekday.short.${it}") }.join(', ')
    }
}
