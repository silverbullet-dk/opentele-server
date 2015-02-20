<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.NextOfKinPerson"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'nextofkin.label', default: 'NextOfKinPerson')}" />
<title><g:message code="default.edit" args="[entityName]" /></title>
</head>

<body>
	<div id="edit-nextOfKinPerson" class="content scaffold-edit" role="main">
		<h1><g:message code="default.edit" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${nextOfKinPersonInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${nextOfKinPersonInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form method="post">
			<g:hiddenField name="id" value="${nextOfKinPersonInstance?.id}" />
			<g:hiddenField name="version" value="${nextOfKinPersonInstance?.version}" />
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="update" value="${message(code: 'default.update')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
