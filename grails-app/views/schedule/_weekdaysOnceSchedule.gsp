<%@ page import="org.opentele.server.core.model.Schedule; org.opentele.server.core.model.types.Weekday" %>
<div id="weekdaysOnceScheduleSetupContainer" class="scheduleType WEEKDAYS_ONCE">

    <div class="fieldcontain ">
        <label data-tooltip="${message(code: 'questionnaireSchedule.blueAlarmTime.toolTip')}"><g:message code="questionnaireSchedule.blueAlarmTime"/></label>

        <div class="timeOfDayInlined">
            <tmpl:/schedule/timeOfDay name="blueAlarmTime" value="${blueAlarmTime}"/>
        </div>
    </div>

    <div class="fieldcontain">
        <label data-tooltip="${message(code: 'questionnaireSchedule.reminderTime.toolTip')}"><g:message code="questionnaireSchedule.reminderTime"/></label>

        <div class="timeOfDayInlined">
            <tmpl:/schedule/timeOfDay name="reminderTime" value="${reminderTime}"/>
        </div>
    </div>
    <tmpl:/schedule/scheduleWindow scheduleType="WEEKDAYS_ONCE"/>

    <div class="fieldcontain">
        <label><g:message code="questionnaireSchedule.introPeriodWeeks"/></label>
        <g:select type="number" name="introPeriodWeeks" from="${(1..8)}"
                  valueMessagePrefix="questionnaireSchedule.introPeriodWeeks.week" value="${introPeriodWeeks}" class="span1"/>
    </div>

    <div class="fieldcontain noborder">
        <label><g:message code="questionnaireSchedule.weekdaysOnce"/></label>

        <div id="weekdaysIntroPeriod" class="weekdays">
            <strong><g:message code="questionnaireSchedule.weekdaysIntroPeriod.label" /></strong><br/>
            <input type="checkbox" id="allNoneIntroPeriod" class="narrow-checkbox">
            <label for="allNoneIntroPeriod" style="width: auto"><g:message code="schedule.allNoDays.label"/></label>
            <br/>
            <g:each in="${Weekday.values()}" var="day">
                <div>
                    <g:checkBox name="weekdaysIntroPeriod" id="weekdaysIntroPeriod_${day}" type="checkbox" value="${day}"
                                checked="${day in weekdaysIntroPeriod}" class="narrow-checkbox"/>
                    <label for="weekdaysIntroPeriod_${day}">
                        <g:message code="enum.weekday.${day}"/>
                    </label>
                </div>
            </g:each>
        </div>

        <div id="weekdaysSecondPeriod" class="weekdays">
            <strong><g:message code="questionnaireSchedule.weekdaysSecondPeriod.label" /></strong><br/>
            <input type="checkbox" id="allNoneSecondPeriod" class="narrow-checkbox">
            <label for="allNoneSecondPeriod" style="width: auto"><g:message code="schedule.allNoDays.label"/></label>
            <br/>
            <g:each in="${Weekday.values()}" var="day">
                <div>
                    <g:checkBox name="weekdaysSecondPeriod" id="weekdaysSecondPeriod_${day}" type="checkbox" value="${day}"
                                checked="${day in weekdaysSecondPeriod}" class="narrow-checkbox"/>
                    <label for="weekdaysSecondPeriod_${day}">
                        <g:message code="enum.weekday.${day}"/>
                    </label>
                </div>
            </g:each>
        </div>
    </div>
</div>
<r:script>
    $('#weekdaysIntroPeriod').weekdays({
        name: "weekdaysIntroPeriod",
        allNoneId: 'allNoneIntroPeriod'
    });
    $('#weekdaysSecondPeriod').weekdays({
        name: "weekdaysSecondPeriod" ,
        allNoneId: 'allNoneSecondPeriod'
    });
</r:script>
