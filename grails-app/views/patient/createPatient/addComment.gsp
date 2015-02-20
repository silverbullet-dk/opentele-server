<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <title>
        <g:message code="patient.create.flow.comments.label"/>
    </title>
</head>
<body>

	<div id="create-patient" class="content scaffold-create" role="main">
		<h1>
			<g:message code="patient.create.flow.comments.label"/>
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
				<g:render template="createPatient/comment" />
			</fieldset>

			<fieldset class="buttons">
                <g:submitButton name="previous" class="goback" value="${message(code: 'patient.create.flow.button.previous.label')}" />
				<g:submitButton name="next" class="gonext" value="${message(code: 'patient.create.flow.button.next.label')}" />
                <g:submitButton name="saveAndShow" class="save" value="${message(code: 'patient.create.flow.button.saveAndExit.label')}" data-tooltip="${message(code: 'patient.create.flow.addComment.finish.tooltip')}"/>
                <g:submitButton name="saveAndGotoMonplan" class="save" value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label')}" data-tooltip="${message(code: 'patient.create.flow.addComment.saveAndExitToMonitoringPlan.tooltip')}"/>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
