<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.MonitorKit"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'monitorKit.label', default: 'MonitorKit')}" />
<title><g:message code="default.edit" args="[entityName]" /></title>
</head>
<body>
	<div id="edit-monitorKit" class="content scaffold-edit" role="main">
		<h1><g:message code="default.edit" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<g:hasErrors bean="${monitorKitInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${monitorKitInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form method="post">
			<g:hiddenField name="id" value="${monitorKitInstance?.id}" />
			<g:hiddenField name="version" value="${monitorKitInstance?.version}" />
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="update" value="${message(code: 'default.update')}" />
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_DELETE}">
                    <g:actionSubmit class="delete" action="delete"
                        value="${message(code: 'default.button.delete.label')}"
                        formnovalidate=""
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                </sec:ifAnyGranted>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
