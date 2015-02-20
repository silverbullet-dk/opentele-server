<%@ page import="org.opentele.server.core.model.types.MeasurementFilterType" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="pageTitle" value="${message(code: 'patientMeasurements.measurements.title', args: [patientInstance.name.encodeAsHTML()])}"/>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bloodsugartable.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'font-awesome.min.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" type="text/css">
</head>

<body>
<script type="text/javascript">
    function showIntervalChooser() {
        $('#custom_interval_chooser').show();
        makeBold($('#${MeasurementFilterType.CUSTOM}'));
    }
    function makeBold(element) {
        element.css({ 'font-weight': 'bold' });
    }
    $(function() {
        if('${params.filter}') {
            makeBold($('#${params.filter}'));
        } else {
            makeBold($('#${MeasurementFilterType.ALL}'));
        }
    })
</script>

<div id="show-patient" class="content scaffold-show" role="main">

    <h1 class="fieldcontain">${pageTitle}</h1>

    <div style="margin: 0.8em 1em 0.3em;padding: 0 0.25em;">
        <g:message code="time.filter.text"/>:
        <a id="${MeasurementFilterType.ALL}" href="${createLink(mapping:"patientMeasurements", params:[patientId: session.patientId])}"><g:message code="time.filter.show.all"/></a> |
        <a id="${MeasurementFilterType.WEEK}" href="${createLink(mapping:"patientMeasurements", params:[filter:"${MeasurementFilterType.WEEK}", patientId: session.patientId])}"><g:message code="time.filter.show.week"/></a> |
        <a id="${MeasurementFilterType.MONTH}" href="${createLink(mapping:"patientMeasurements", params:[filter:"${MeasurementFilterType.MONTH}", patientId: session.patientId])}"><g:message code="time.filter.show.month"/></a> |
        <a id="${MeasurementFilterType.QUARTER}" href="${createLink(mapping:"patientMeasurements", params:[filter:"${MeasurementFilterType.QUARTER}", patientId: session.patientId])}"><g:message code="time.filter.show.quarter"/></a> |
        <a id="${MeasurementFilterType.YEAR}" href="${createLink(mapping:"patientMeasurements", params:[filter:"${MeasurementFilterType.YEAR}", patientId: session.patientId])}"><g:message code="time.filter.show.year"/></a> |
        <a id="${MeasurementFilterType.CUSTOM}" href="#" onclick="showIntervalChooser();">${message(code: 'time.filter.show.choose')}</a>
    </div>
    <g:if test="${MeasurementFilterType.valueOf(params.filter) == MeasurementFilterType.CUSTOM}">
        <div id="custom_interval_chooser">
    </g:if>
    <g:else>
        <div id="custom_interval_chooser" style="display:none">
    </g:else>
        <g:form method="GET" mapping='patientMeasurements' params='[patientId:session.patientId]'>
            <fieldset class="form">
                <div class="fieldcontain">
                    <label for="start" style="width:auto"><g:message code="time.filter.custom.start"/>:</label>
                    <jq:datePicker name="start" precision="day" value="${params.start}"/>
                    <label for="end" style="width:auto"><g:message code="time.filter.custom.end"/>:</label>
                    <jq:datePicker name="end" precision="day" value="${params.end}"/>
                </div>
                <fieldset class="buttons">
                    <g:hiddenField name="filter" value="${MeasurementFilterType.CUSTOM}"/>
                    <input type="Submit" value="${g.message(code: 'time.filter.submit')}" class="acknowledge" style="float:left"/>
                </fieldset>
            </fieldset>
        </g:form>
    </div>

    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>

    <g:if test="${bloodSugarData}">
        <h1 class="fieldcontain">
            <g:message code="patient.overview.bloodsugar"/>
            <g:link mapping="patientMeasurementBloodsugar"
                    params="${[patientId: patientInstance.id, filter: filter.type]}">
                <g:img file="print.png" class="bloodsugarPrint"/>
            </g:link>
        </h1>
        <g:render template="bloodSugar" model="bloodSugarData"/>
    </g:if>

    <g:if test="${tables}">
        <h1 class="fieldcontain">
            <g:message code="patient.overview.measurements" args="[patientInstance.name.encodeAsHTML()]"/>
        </h1>
        <ol class="property-list">
            <span class="property-value">
                <g:each in="${tables}" var="tableData">
                    <h1>${message(code: "patient.overview.table.label.${tableData.type}")}</h1>
                    <div id="list-patient" class="content scaffold-list">
                        <table>
                            <thead>
                                <tr>
                                    <th><g:message code="measurement.time.label"/></th>
                                    <th><g:message code="measurement.value.label"/></th>
                                    <th><g:message code="measurement.unit.label"/></th>
                                    <th><g:message code="measurement.meter.label"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${tableData.measurements}" status="i" var="measurement">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            ${g.formatDate(date:measurement.time())}
                                            <g:if test="${Boolean.valueOf(grailsApplication.config.kihdb.run) && measurement.shouldBeExportedToKih() && !measurement.exportedToKih()}">
                                                <g:img file="notExportedToKIH.png" style="vertical-align:middle" data-tooltip="${message(code: 'patient.overview.measurement.not.exported')}"/>
                                            </g:if>
                                            <g:if test="${measurement.conference()}">
                                                <g:img file="conferenceshow.png" width="20" height="20"
                                                       style="vertical-align:middle" data-tooltip="${message(code: 'tooltip.measurement.isfromconference')}"
                                                />
                                            </g:if>
                                            <g:if test="${measurement.consultation()}">
                                                <g:img file="consultationaddmeasurements.png" width="20" height="20"
                                                       style="vertical-align:middle" data-tooltip="${message(code: 'tooltip.measurement.isfromconsultation')}"
                                                />
                                            </g:if>
                                        </td>
                                        <td class="${measurement.alert() ? 'alert' : (measurement.warning() ? 'warning' : '')}">${measurement.value()}</td>
                                        <td>${message(code: "enum.unit.${measurement.unit()}")}</td>
                                        <td>${measurement.meterModel()}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
					</div>
                </g:each>
            </span>
        </ol>
    </g:if>
    <g:else>
        <p><g:message code="measurements.show.nomeasurements"/></p>
    </g:else>
</div>
</body>
</html>
