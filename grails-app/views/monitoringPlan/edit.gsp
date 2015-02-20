<%@ page import="org.opentele.server.model.MonitoringPlan"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'monitoringPlan.edit.title', args: [monitoringPlanInstance.patient.name.encodeAsHTML()])}" />
    <title>${title}</title>
</head>

<body>
	<div id="edit-monitoringPlan" class="content scaffold-edit" role="main">
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
		<g:form method="post">
			<g:hiddenField name="id" value="${monitoringPlanInstance?.id}" />
			<g:hiddenField name="version" value="${monitoringPlanInstance?.version}" />
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="update"
					value="${message(code: 'default.update', default: 'Update')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
