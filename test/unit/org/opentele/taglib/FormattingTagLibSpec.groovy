package org.opentele.taglib

import grails.test.mixin.TestFor
import org.opentele.server.core.model.Schedule
import spock.lang.Specification
import spock.lang.Unroll


@TestFor(FormattingTagLib)
class FormattingTagLibSpec extends Specification {
    @Unroll
    def "test that prettyStringForScheduleType returns the correct translation"() {
        expect:
        tagLib.prettyStringForScheduleType(message: message) == output

        where:
        message                             | output
        Schedule.ScheduleType.MONTHLY       | "schedule.scheduleType.MONTHLY.label"
        Schedule.ScheduleType.EVERY_NTH_DAY | "schedule.scheduleType.EVERY_NTH_DAY.label"
        Schedule.ScheduleType.EVERY_NTH_DAY | "schedule.scheduleType.EVERY_NTH_DAY.label"
    }
}
