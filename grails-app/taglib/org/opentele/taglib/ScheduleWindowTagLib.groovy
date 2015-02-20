package org.opentele.taglib

import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.Schedule.ScheduleType
import org.opentele.server.model.ScheduleWindow

class ScheduleWindowTagLib {

    static namespace = "scheduleWindow"

    def prettyformat = { attrs ->
        Integer minutes = attrs.remove('minutes')
        def scheduleType = attrs.remove('scheduleType') // String or ScheduleType
        if (scheduleType) {
            minutes = resolveScheduleType(scheduleType)?.windowSizeMinutes
        }

        if (minutes) {

            def days = minutes.intdiv(24 * 60)
            minutes = minutes - 24 * 60 * days
            def hours = minutes.intdiv(60)
            minutes = minutes - 60 * hours
            def text = []
            if (days) {
                text << message(code: days == 1 ? 'duration.prettyformat.day' : 'duration.prettyformat.days', args: [days])
            }
            if (hours) {
                text << message(code: hours == 1 ? 'duration.prettyformat.hour' : 'duration.prettyformat.hours', args: [hours])
            }
            if (minutes) {
                text << message(code: minutes == 1 ? 'duration.prettyformat.minute' : 'duration.prettyformat.minutes', args: [minutes])
            }
            log.debug("$days $hours $minutes ${text}")
            String output = text.join(', ')
            def pos = output.lastIndexOf(', ')
            if (pos > 0) {
                output = "${output[0..pos - 1]} ${message(code: 'duration.prettyformat.and')}${output[pos + 1..-1]}"
            }
            out << output
        } else {
            log.warn("Missing [scheduleType] or [minutes] attributes/values in [scheduleWindow:prettyformat] taglib. Nothing to display")
        }

    }

    private resolveScheduleType(def scheduleType) {
        if(!scheduleType instanceof Schedule.ScheduleType) {
            try {
                scheduleType = ScheduleType.valueOf(scheduleType.toString())
            } catch (ignore) {
                return null
            }
        }
        ScheduleWindow.findByScheduleType(scheduleType as ScheduleType)
    }
}
