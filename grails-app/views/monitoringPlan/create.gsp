<%@ page import="org.opentele.server.model.MonitoringPlan"%>
<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'monitoringPlan.create.title', args: [patientInstance.name.encodeAsHTML()])}" />
    <title>${title}</title>
</head>
<body>
	<div id="create-monitoringPlan" class="content scaffold-create" role="main">
        <h1>${title}</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${monitoringPlanInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${monitoringPlanInstance}" var="error">
					<li
						<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
							error="${error}" /></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form action="save">
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<div>
					<g:submitButton name="create" class="save"
						value="${message(code: 'default.button.create.label', default: 'Create')}"
						data-tooltip="${message(code: 'tooltip.patient.monitoringPlan.create')}" />
					<g:link class="cancel" action="show" id="${session.patientId}">
						<g:message code="default.button.goback.label" />
					</g:link>
				</div>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
