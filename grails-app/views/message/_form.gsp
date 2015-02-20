<%@ page import="org.opentele.server.model.Message"%>
<%@ page import="org.opentele.server.provider.constants.Constants"%>


<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'patient', 'error')} required">
	<label for="patient">
        <g:message code="message.to.patient.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:if test="${patient != null}">
		<g:hiddenField name="patient" value="${patient.id}" />
		${patient.name()}
	</g:if>
	<g:else>
		<g:select id="patient" name="patient" from="${recepients}"
			optionKey="id" required="" value="${name}" class="many-to-one" />
	</g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'department', 'error')} required">
	<label for="department">
        <g:message code="message.from.department.label" />
		<span class="required-indicator">*</span>
	</label>
	<g:if test="${departments.size() > 1}">
		<g:select id="department" name="department" from="${departments}"
			optionKey="id" required="" value="${name}" />
	</g:if>
	<g:else>
		${departments[0].name}
		<g:hiddenField name="department" value="${departments[0].id}" />
	</g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'title', 'error')} ">
	<label for="title">
        <g:message code="message.title.label" />
	</label>
	<g:textField name="title" value="${messageInstance?.title}" />
</div>

<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'text', 'error')} ">
	<label for="text">
        <g:message code="message.text.label" />
	</label>
	<g:textArea name="text" cols="40" rows="5" maxlength="2000" value="${messageInstance?.text}" />
</div>
