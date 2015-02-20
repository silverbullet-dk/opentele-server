<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireGroup" %>



<div class="fieldcontain ${hasErrors(bean: questionnaireGroupInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="questionnaireGroup.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${questionnaireGroupInstance?.name}"/>
</div>



