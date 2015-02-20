<%@ page import="grails.converters.JSON; org.opentele.server.core.model.Schedule; org.opentele.server.model.QuestionnaireSchedule" %>
<%@ page import="org.opentele.server.core.model.types.Weekday" %>
<%@ page import="org.opentele.server.core.model.Schedule.ScheduleType" %>

<g:hiddenField name="questionnaireGroup.id" value="${questionnaireGroup?.id}"/>

<div class="fieldcontain required">
    <label for="selectedQuestionnaireHeader.id">
        <g:message code="questionnaireGroup2QuestionnaireHeader.patientQuestionnaire.label"/>
    </label>
    <g:select name="selectedQuestionnaireHeader.id" from="${selectableQuestionnaireHeaders}" optionKey="id" optionValue="name" value="${selectedQuestionnaireHeader?.id}"/>
</div>

<div class="fieldcontain">
    <label>
        <g:message code="questionnaireGroup2QuestionnaireHeader.schedule.label"/>
    </label>


<div class="scheduleTypeSelector">
<div>
    <label for="inherited"
           data-tooltip="${message(code: 'questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.inherit.tooltip')}">
        <g:radio id="inherited" name="type" value="" checked="${!type}"/>
        <g:message code="questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.inherit.label"/>
    </label>
</div>
<g:each in="${ScheduleType.values()}" var="scheduleType">
    <div>
    <label for="unscheduled_${scheduleType}"
           data-tooltip="${message(code: "questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.${scheduleType}.tooltip")}">
        <g:radio id="unscheduled_${scheduleType}" name="type" value="${scheduleType}" checked="${type == scheduleType}"/>
        <g:message code="schedule.scheduleType.${scheduleType.toString()}.label"/>
    </label>
    </div>
</g:each>
</div>
</div>

<tmpl:/schedule/scheduleTypes/>

