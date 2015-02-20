<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">

    <title><g:message code="responsible.patient.group.edit.label"/></title>
</head>

<body>
	<div id="edit-patient" class="content scaffold-edit" role="main">
		<h1><g:message code="responsible.patient.group.edit.label"/></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:if test="${flash.error}">
			<div class="errors" role="status">
				${flash.error}
			</div>
		</g:if>

		<g:hasErrors bean="${patientInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${patientInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>

		<g:form method="post">
			<g:hiddenField name="id" value="${patientInstance?.id}" />
			<fieldset class="form">
				<g:render template="formResponsability" />
			</fieldset>
			<fieldset class="buttons">

                <g:actionSubmit class="save" action="updateDataResponsible" value="${message(code: 'default.update')}" />
			</fieldset>
		</g:form>

	</div>
</body>
</html>
