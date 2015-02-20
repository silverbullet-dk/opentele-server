<%@ page import="org.opentele.server.model.BloodPressureThreshold; org.opentele.server.model.UrineGlucoseThreshold; org.opentele.server.model.UrineThreshold" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="threshold.edit.${command.threshold.type}" /></title>
	</head>
	<body>
		<div id="edit-standardThreshold" class="content scaffold-edit" role="main">
            <g:if test="${patientGroup}">
                <h1><g:message code="threshold.editForPatientGroup.${command.threshold.type}" args="[patientGroup.name]" /></h1>
            </g:if>
            <g:else>
                <h1><g:message code="threshold.edit.${command.threshold.type}" /></h1>
            </g:else>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${command}">
			<ul class="errors" role="alert">
				<g:eachError bean="${command}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                    <g:message error="${error}"/>
                </li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${command.threshold?.id}" />
				<g:hiddenField name="threshold.id" value="${command.threshold?.id}" />
				<g:hiddenField name="version" value="${command.threshold?.version}" />
                <g:hiddenField name="ignoreNavigation" value="true"/>
				<fieldset class="form">
                    <g:if test="${command.threshold.instanceOf(UrineThreshold)}">
                        <tmpl:/urineThreshold/form standardThresholdInstance="${command}"/>
                    </g:if>
                    <g:elseif test="${command.threshold.instanceOf(UrineGlucoseThreshold)}">
                        <tmpl:/urineGlucoseThreshold/form standardThresholdInstance="${command}"/>
                    </g:elseif>
                    <g:elseif test="${command.threshold.instanceOf(BloodPressureThreshold)}">
                        <tmpl:/bloodPressureThreshold/form standardThresholdInstance="${command}"/>
                    </g:elseif>
                    <g:else>
                        <tmpl:/numericThreshold/form standardThresholdInstance="${command}"/>
                    </g:else>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
