<%@ page import="org.opentele.server.model.MonitoringPlan"%>

<p>
	<i><g:message code="monitoringPlan.description"/></i>
</p>
<div
	class="fieldcontain ${hasErrors(bean: monitoringPlanInstance, field: 'startDate', 'error')} required">
	<label for="startDate">
        <g:message code="monitoringPlan.startDate.label" />
        <span class="required-indicator">*</span>
	</label>
    <jq:datePicker name="datePicker" precision="day" value="${monitoringPlanInstance?.startDate}"/>
</div>
