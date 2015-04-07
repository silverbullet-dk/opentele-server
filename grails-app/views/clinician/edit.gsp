'<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Clinician"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'default.user.label', default: 'Bruger')}" />
<title><g:message code="default.edit" args="[entityName]" /></title>
</head>
<body>
	<div id="edit-clinician" class="content scaffold-edit" role="main">
		<h1><g:message code="default.edit" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${clinicianInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${clinicianInstance}" var="error">
					<li	<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>
        <g:hasErrors bean="${cmd}">
            <ul class="errors" role="alert">
                <g:eachError bean="${cmd}" var="error">
                    <li> <g:message error="${error}" /></li>
                </g:eachError>
            </ul>
        </g:hasErrors>

		<g:form method="post" id="${cmd?.id}">
			<g:hiddenField name="version" value="${cmd?.version}" />
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
