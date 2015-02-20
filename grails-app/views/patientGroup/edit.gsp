<%@ page import="org.opentele.server.model.PatientGroup" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
        <g:set var="entityName"
               value="${message(code: 'patientGroup.label', default: 'Patient group')}" />
        <title><g:message code="default.edit" args="[entityName]"/></title>
    </head>
	<body>
		<div id="edit-patientGroup" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${patientGroupInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${patientGroupInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${patientGroupInstance?.id}" />
				<g:hiddenField name="version" value="${patientGroupInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.update', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'patientGroup.delete.message.confirm', args: [patientGroupInstance?.name, patientGroupInstance?.department])}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
