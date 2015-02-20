<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="patient.consultation.title"/></title>
</head>

<body>
<div id="show-patient" class="content scaffold-show" role="main">
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>

    <h1 class="fieldcontain"><g:message code="patient.consultation" /></h1>

    <ol class="property-list patient">
        <li class="fieldcontain">
            <span class="property-label">
                <g:message code="patient.consultation.heldByClinician"/>:
            </span>
            <span class="property-value" aria-labelledby="user-label">
                ${consultation.clinician.name}
            </span>
        </li>
        <li class="fieldcontain">
            <span class="property-label">
                <g:message code="patient.consultation.heldAt" />:
            </span>
            <span class="property-value" aria-labelledby="user-label">
                <g:formatDate formatName="default.date.format" date="${consultation.createdDate}" />
            </span>
        </li>
        <li class="fieldcontain">
            <span class="property-label">
                <g:message code="patient.measurements.label" />:
            </span>
        </li>
    </ol>
    <consultation:measurementsTable consultation="${consultation}"/>
</div>
</body>
</html>
