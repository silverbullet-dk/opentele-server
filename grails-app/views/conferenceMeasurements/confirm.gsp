<%@ page import="org.opentele.server.provider.constants.Constants; org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.core.model.ConferenceMeasurementDraftType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="conferenceMeasurements">

    <title><g:message code="conferenceMeasurement.confirm.title"/></title>
</head>

<body>
<h1><g:message code="conferenceMeasurement.confirm.header"/></h1>
<p>
    <span id="patent_name">${session[Constants.SESSION_NAME]} </span>
    <span id="patient_cpr"><g:message code="main.SSN"/>: ${session[Constants.SESSION_CPR]} </span>
</p>
<p style="text-align: center"><g:message code="conferenceMeasurement.confirm.description"/></p>
<table>
    <thead>
    <tr>
        <th><g:message code="conferenceMeasurement.type"/></th>
        <th><g:message code="conferenceMeasurement.value"/></th>
    </tr>
    </thead>
    <tbody id="measurements">
        <g:if test="${measurementProposals.empty}">
            <tr>
                <td colspan="2"><g:message code="conferenceMeasurement.confirm.noMeasurements"/></td>
            </tr>
        </g:if>
        <g:else>
            <g:each in="${measurementProposals}" var="measurementProposal">
                <tr>
                    <g:if test="${measurementProposal.measurementType.name == MeasurementTypeName.BLOOD_PRESSURE}">
                        <g:render template="confirmations/bloodPressure" model="[measurement: measurementProposal]"/>
                    </g:if>
                    <g:elseif test="${measurementProposal.measurementType.name == MeasurementTypeName.LUNG_FUNCTION}">
                        <g:render template="confirmations/lungFunction" model="[measurement: measurementProposal]"/>
                    </g:elseif>
                    <g:elseif test="${measurementProposal.measurementType.name == MeasurementTypeName.SATURATION}">
                        <g:render template="confirmations/saturation" model="[measurement: measurementProposal]"/>
                    </g:elseif>
                    <g:elseif test="${measurementProposal.measurementType.name == MeasurementTypeName.WEIGHT}">
                        <g:render template="confirmations/weight" model="[measurement: measurementProposal]"/>
                    </g:elseif>
                    <g:elseif test="${measurementProposal.measurementType.name == MeasurementTypeName.PULSE}">
                        <g:render template="confirmations/pulse" model="[measurement: measurementProposal]"/>
                    </g:elseif>
                </tr>
            </g:each>
        </g:else>
    </tbody>
    <tfoot>
        <tr>
            <td>
                <button onclick="window.location.href='${createLink(action:"show", id:"${conference.id}")}'"><g:message code="conferenceMeasurement.confirm.goBackAndEdit"/></button>
            </td>
            <td>
                <g:form action="finish" id="${conference.id}">
                    <g:hiddenField name="conferenceVersion" value="${conference.version}"/>
                    <g:submitButton name="finish" value="${g.message(code: 'conferenceMeasurement.confirm.finish')}"/>
                </g:form>
            </td>
        </tr>
    </tfoot>
</table>
</body>
</html>