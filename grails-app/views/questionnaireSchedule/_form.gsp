<%@ page import="grails.converters.JSON; org.opentele.server.model.QuestionnaireSchedule" %>
<%@ page import="org.opentele.server.core.model.types.Weekday" %>
<%@ page import="org.opentele.server.core.model.Schedule.ScheduleType" %>
<link href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" rel="stylesheet">

<g:hiddenField name="monitoringPlan.id" value="${monitoringPlan?.id}"/>
<div class="fieldcontain ${hasErrors(bean: questionnaireSchedule, field: 'patientQuestionnaire', 'error')} required">
    <label for="selectedQuestionnaireHeader.id">
        <g:message code="questionnaireSchedule.patientQuestionnaire.label"/> <span class="required-indicator">*</span>
    </label>
    <g:select name="selectedQuestionnaireHeader.id" id="selectedQuestionnaireHeaderId" from="${selectableQuestionnaireHeaders}" optionKey="id" optionValue="name" value="${selectedQuestionnaireHeader?.id}"/>
</div>
<div class="fieldcontain">
    <label>
        <g:message code="questionnaireSchedule.schedule.label"/>:
    </label>

    <div class="scheduleTypeSelector">
    <g:each in="${ScheduleType.values()}" var="scheduleType">
        <div>
            <label for="unscheduled_${scheduleType}"
                   data-tooltip="${message(code: "schedule.scheduleType.${scheduleType.toString()}.tooltip")}">
                <g:radio id="unscheduled_${scheduleType}" name="type" value="${scheduleType}" checked="${scheduleType == type}" />
                <g:message code="schedule.scheduleType.${scheduleType.toString()}.label"/>
            </label>
        </div>
    </g:each>
    </div>
</div>

<tmpl:/schedule/scheduleTypes/>


