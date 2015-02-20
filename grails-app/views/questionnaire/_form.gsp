<%@ page import="org.opentele.server.model.questionnaire.Questionnaire"%>



<div class="fieldcontain ${hasErrors(bean: questionnaireInstance, field: 'name', 'error')} ">
	<label for="name">
        <g:message code="questionnaire.name.label" default="Name" />
	</label>
	<g:textField name="name" value="${questionnaireInstance?.name}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: questionnaireInstance, field: 'revision', 'error')} ">
	<label for="revision">
        <g:message code="questionnaire.revision.label" default="Revision" />
	</label>
	<g:textField name="revision" value="${questionnaireInstance?.revision}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: questionnaireInstance, field: 'creator', 'error')} required">
	<label for="creator">
        <g:message code="questionnaire.creator.label" default="Creator" />
        <span class="required-indicator">*</span>
	</label>
	<g:select id="creator" name="creator.id"
		from="${org.opentele.server.model.Clinician.list()}" optionKey="id"
		required="" value="${questionnaireInstance?.creator?.id}"
		class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: questionnaireInstance, field: 'creationDate', 'error')} required">
	<label for="creationDate">
        <g:message code="questionnaire.creationDate.label" default="Creation Date" />
        <span class="required-indicator">*</span>
	</label>
    <jq:datePicker name="creationDate" precision="day" value="${questionnaireInstance?.creationDate}" />
</div>
