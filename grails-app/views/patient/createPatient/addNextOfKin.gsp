<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <title>
        <g:message code="patient.create.flow.addNextOfKin.label"/>
    </title>
</head>
<body>

<div id="create-patient" class="content scaffold-create" role="main">
    <h1>
        <g:message code="patient.create.flow.addNextOfKin.label"/>
    </h1>

    <g:if test="${error}">
        <div class="errors" role="status">
            ${error}
        </div>
    </g:if>


    <g:form>
        <fieldset class="form">
                <ul class="one-to-many">
                    <g:each in="${patientInstance.nextOfKins}" var="nextOfKin">
                        <li>
                            ${nextOfKin.nameAndRelation?.encodeAsHTML()} <g:message code="nextOfKinPerson.phone.label"/>: ${nextOfKin.phone?.encodeAsHTML()}
                        </li>
                    </g:each>
                </ul>
        </fieldset>

        <fieldset class="buttons">
            <g:submitButton name="previous" class="goback" value="${message(code: 'patient.create.flow.button.previous.label')}" />
            <g:submitButton name="create" class="create" value="${message(code: 'patient.create.flow.button.createNextOfKin.label')}" />
            <g:submitButton name="next" class="gonext" value="${message(code: 'patient.create.flow.button.next.label')}" />
            <g:submitButton name="saveAndShow" class="save" value="${message(code: 'patient.create.flow.button.saveAndExit.label')}" data-tooltip="${message(code: 'patient.create.flow.addNextOfKin.finish.tooltip')}"/>
            <g:submitButton name="saveAndGotoMonplan" class="save" value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label')}" data-tooltip="${message(code: 'patient.create.flow.addNextOfKin.saveAndExitToMonitoringPlan.tooltip')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
