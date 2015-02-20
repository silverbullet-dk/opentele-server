<%@ page import="org.opentele.server.model.PassiveInterval" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="passiveInterval.edit" /></title>
	</head>
	<body>
		<div id="edit-passiveInterval" class="content scaffold-edit" role="main">
			<h1><g:message code="passiveInterval.edit" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${passiveIntervalInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${passiveIntervalInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${passiveIntervalInstance?.id}" />
				<g:hiddenField name="version" value="${passiveIntervalInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
