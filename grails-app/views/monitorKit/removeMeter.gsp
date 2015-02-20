<%@ page import="org.opentele.server.model.MonitorKit"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'monitorKit.label', default: 'MonitorKit')}" />
<title><g:message code="monitorkit.remove.meter.label" /></title>
</head>

<body>
	<div id="edit-monitorKit" class="content scaffold-edit" role="main">
		<h1><g:message code="monitorkit.remove.meter.label" /></h1>
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
		<g:if test="${meters.size > 0 }">
			<g:form method="post">
				<g:hiddenField name="id" value="${monitorKitInstance?.id}" />
				<g:hiddenField name="version" value="${monitorKitInstance?.version}" />

				<div class="fieldcontain">
					<label for="addMeter">
                        <g:message code="monitorkit.remove.meter.label" default="Department" />
					</label>
					<g:select name="meter.id" from="${meters}" value="${name}" optionKey="id" />
				</div>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="removeMeter" value="${message(code: 'default.button.save.label')}" />
				</fieldset>
			</g:form>
		</g:if>
		<g:else>
			<div class="fieldcontain">
				<g:message code="monitorkit.no.meters.to.remove" />
			</div>
		</g:else>
	</div>
</body>
</html>
