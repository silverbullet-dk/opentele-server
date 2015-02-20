<%@ page import="org.opentele.server.model.ScheduleWindow" %>



<div class="fieldcontain ${hasErrors(bean: scheduleWindowInstance, field: 'scheduleType', 'error')} required">
	<label for="scheduleType">
		<g:message code="scheduleWindow.scheduleType.label" default="Schedule Type" />
	</label>
    <span class="property-value" aria-labelledby="scheduleType-label"><otformat:prettyStringForScheduleType  message="${scheduleWindowInstance.scheduleType}"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: scheduleWindowInstance, field: 'windowSizeMinutes', 'error')} required">
	<label for="windowSizeMinutes">
		<g:message code="scheduleWindow.windowSizeMinutes.label" default="Window Size Minutes" />
		<span class="required-indicator">*</span>
	</label>
    <g:textField name="windowSizeMinutes" value="${scheduleWindowInstance?.windowSizeMinutes}" /> <g:message code="scheduleWindow.minutes"/>
</div>

