<%@ page import="org.opentele.server.model.Clinician"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'patient.group.label', default: 'Clinician')}" />
<title><g:message code="default.remove.label"
		args="[entityName]" /></title>
</head>
<body>
	<div id="remove-patientGroup" class="content scaffold-create"
		role="main">
		<h1>
			<g:message code="default.remove.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${clinicianInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${clinicianInstance}" var="error">
					<li
						<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
							error="${error}" /></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form action="doRemovePatientGroup">
			<g:hiddenField name="clinician.id" value="${clinicianInstance.id}" />
			<fieldset class="form">

				<%@ page import="org.opentele.server.model.Clinician2PatientGroup"%>

				<div
					class="fieldcontain ${hasErrors(bean: clinician2PatientGroupInstance, field: 'patientGroup', 'error')} required">
					<label for="patientGroup"> <g:message
							code="clinician2PatientGroup.patientGroup.label"
							default="Patient Group" /> <span class="required-indicator">*</span>
					</label>
					<g:select id="patientGroup" name="patientGroup.id"
						from="${clinicianPatientGroups}" optionKey="id"
						class="many-to-one" />
				</div>
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="remove" class="delete"
					value="${message(code: 'default.button.remove.label2', default: 'Remove')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>

