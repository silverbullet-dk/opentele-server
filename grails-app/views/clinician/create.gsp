<%@ page import="org.opentele.server.model.Clinician"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'default.user.label', default: 'Bruger')}" />
<title><g:message code="default.create.label"
		args="[entityName]" /></title>
</head>
<body>

	<div id="create-clinician" class="content scaffold-create" role="main">
		<h1>
			<g:message code="default.create.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
        <g:if test="${errors}">
            <ul class="errors" role="alert">
                <g:each in="${errors}" var="error">
                    <li> <g:message error="${error}" /></li>
                </g:each>
            </ul>
        </g:if>
        <g:hasErrors bean="${cmd}">
            <ul class="errors" role="alert">
                <g:eachError bean="${cmd}" var="error">
                    <li> <g:message error="${error}" /></li>
                </g:eachError>
            </ul>
        </g:hasErrors>
        <g:form action="save">
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="create" class="save"
					value="${message(code: 'default.button.create.label', default: 'Create')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
