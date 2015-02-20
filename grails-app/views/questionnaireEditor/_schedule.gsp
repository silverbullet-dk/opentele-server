<%@ page import="org.opentele.server.core.model.Schedule.ScheduleType" %>
<%@ page import="org.opentele.server.core.model.types.Weekday" %>

<tooltip:resources />
<span class="nav-header"><g:message code="questionnaireEditor.schedule"/> </span>

<g:each in="${ScheduleType.values()}" var="scheduleType">
<div class="fieldcontain noborder">
    <label class="radio" data-tooltip="${message(code: "schedule.scheduleType.${scheduleType}.tooltip")}">
        <g:radio id="unscheduled_${scheduleType}" name="type" value="${scheduleType}" checked="${scheduleType == type}" />
        <g:message code="schedule.scheduleType.${scheduleType}.label" />
    </label>
</div>
</g:each>
<tmpl:/schedule/scheduleTypes/>
