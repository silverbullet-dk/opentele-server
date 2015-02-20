<%@ page import="org.opentele.server.core.model.types.PatientState; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'patient.label', default: 'Patient')}" />
<title><g:message code="default.edit" args="[entityName]" /></title>
    <link href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" rel="stylesheet">
</head>

<body>
	<div id="edit-patient" class="content scaffold-edit" role="main">
		<h1><g:message code="default.edit" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:if test="${flash.error}">
			<div class="errors" role="status">
				${flash.error}
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

		<g:form method="post" name="edit-patient-form">
			<g:hiddenField name="id" value="${patientInstance?.id}" />
			<g:hiddenField name="version" value="${patientInstance?.version}" />
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>

            <g:javascript>
                $('#edit-patient-form').submit(function() {
                    var oldState = '${patientInstance?.state}';
                    var selectedState = $(this).find('select#state option:selected');
                    var stateVal = selectedState.val();

                    if (stateVal !== oldState && ( stateVal === '${PatientState.DECEASED}' || stateVal === '${PatientState.DISCHARGED_EQUIPMENT_DELIVERED}'
                        || stateVal === '${PatientState.DISCHARGED_EQUIPMENT_NOT_DELIVERED}' )) {

                        var message = '${message(code: "patient.edit.changeState.warning")} "' + selectedState.text() + '"';
                        return confirm(message);
                    }
                });
            </g:javascript>

            <fieldset class="buttons">
				<g:actionSubmit class="save" action="update" value="${message(code: 'default.update')}" />
			</fieldset>
		</g:form>

    </div>

<div id="edit-patient" class="content scaffold-edit" role="main">
    <h1><g:message code="patient.edit.thresholds.label"/></h1>
    <g:render template="thresholds"
              model="[thresholds: patientInstance.thresholds, parent: patientInstance, writePermission: PermissionName.THRESHOLD_WRITE, deletePermission: PermissionName.THRESHOLD_DELETE]">
        <sec:ifAnyGranted roles="${PermissionName.PATIENT_WRITE}">
            <tfoot>
            <tr><td colspan="100">
            <g:form method="post" action="addThreshold">
                <g:hiddenField name="id" value="${patientInstance.id}"/>
                <fieldset class="buttons">
                    <g:submitButton name="chooseThreshold"
                                    value="${message(code: 'patient.threshold.add.label', default: 'Add')}"/>
                </fieldset>

            </g:form>
            </td></tr>
            </tfoot>
        </sec:ifAnyGranted>
    </g:render>
</div>
</body>
</html>
