<%@ page import="org.opentele.server.core.model.types.Severity; org.opentele.server.model.questionnaire.QuestionnaireHeader" %>

<div class="fieldcontain ${hasErrors(bean: questionnaireHeaderInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="questionnaireHeader.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${questionnaireHeaderInstance.name}"/>
</div>

<div
    class="fieldcontain ${hasErrors(bean: questionnaireHeaderInstance, field: 'requiresManualInpection', 'error')} required"
    data-tooltip="${message(code: 'tooltip.questionnaireHeader.requiresManualInspection')}">
    <label for="requiresManualInspection">
        <g:message code="questionnaireHeader.requiresManualInspection.label" />
    </label>

    <g:checkBox name="requiresManualInspection" value="${questionnaireHeaderInstance.requiresManualInspection}" style="height:12px"/>

</div>

