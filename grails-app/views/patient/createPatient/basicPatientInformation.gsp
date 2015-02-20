<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title>
        <g:message code="patient.create.flow.basicInfo.label" />
    </title>
</head>
<body>

	<div id="create-patient" class="content scaffold-create" role="main">
		<h1>
			<g:message code="patient.create.flow.basicInfo.label"/>
		</h1>
        <g:if test="${error}">
            <div class="errors" role="status">
                ${error}
            </div>
        </g:if>
		<g:hasErrors bean="${patientInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${patientInstance}" var="error">
                    <li>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>

		<g:form>
			<fieldset class="form">
				<g:render template="createPatient/basicForm" />
			</fieldset>

			<fieldset class="buttons">
				<g:submitButton name="next" class="gonext" value="${message(code: 'patient.create.flow.button.next.label')}" />
                <!-- ${grailsApplication.config.cpr.lookup.enabled} -->
                <g:if test="${grailsApplication.config.cpr.lookup.enabled}">
                    <g:submitButton name="lookupCPR" class="search" value="${message(code: 'patient.create.flow.button.CPRlookup.label')}" />
                </g:if>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
