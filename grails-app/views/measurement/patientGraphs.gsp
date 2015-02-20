<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.core.model.types.MeasurementFilterType" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="pageTitle" value="${message(code: 'patientMeasurements.graphs.title', args: [patientInstance.name.encodeAsHTML()])}"/>
    <title>${pageTitle}</title>
    <g:set var="showThresholds" value="${true}" scope="request"/>
    <g:set var="graphForPatient" value="${false}" scope="request"/>
    <g:render template="graphFunctions"/>
	<g:render template="measurementGraph" collection="${measurements}" var="measurement" model="${[patient: patientInstance, patientIdForFullScreen: params.patientId]}"/>
    <g:render template="bloodSugarMeasurementGraphs" collection="${measurements}" var="measurement" model="${[patient: patientInstance, patientIdForFullScreen: params.patientId]}"/>
    <g:render template="continuousBloodSugarMeasurementGraphs" collection="${measurements}" var="measurement" model="${[patient: patientInstance, patientIdForFullScreen: params.patientId]}"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bloodsugartable.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'font-awesome.min.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.jqplot.css')}" type="text/css">
    <style>
    #slider {
        margin-left: 20px;
        margin-right: 20px;
    }
    </style>
    <g:javascript>
        $(function() {
            var margin = 20; // The "margin-left" and "margin-right" values, above
            var slider = $('#slider');
            $('#marker').hide();
            slider.slider({
                min: 0,
                max: $('#overlays').width() - 2*margin,
                slide: function( event, ui ) {
                    var value = ui.value;
                    var offset = value + margin;
                    $('#marker').show();
                    $('#marker').css('margin-left', offset + 'px');
                }
            });


        });
    </g:javascript>
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
        <a id="${MeasurementFilterType.ALL}" href="${createLink(mapping:"patientGraphs", params:[patientId: session.patientId])}"><g:message code="time.filter.show.all"/></a> |
        <a id="${MeasurementFilterType.WEEK}" href="${createLink(mapping:"patientGraphs", params:[filter:"${MeasurementFilterType.WEEK}", patientId: session.patientId])}"><g:message code="time.filter.show.week"/></a> |
        <a id="${MeasurementFilterType.MONTH}" href="${createLink(mapping:"patientGraphs", params:[filter:"${MeasurementFilterType.MONTH}", patientId: session.patientId])}"><g:message code="time.filter.show.month"/></a> |
        <a id="${MeasurementFilterType.QUARTER}" href="${createLink(mapping:"patientGraphs", params:[filter:"${MeasurementFilterType.QUARTER}", patientId: session.patientId])}"><g:message code="time.filter.show.quarter"/></a> |
        <a id="${MeasurementFilterType.YEAR}" href="${createLink(mapping:"patientGraphs", params:[filter:"${MeasurementFilterType.YEAR}", patientId: session.patientId])}"><g:message code="time.filter.show.year"/></a> |
        <a id="${MeasurementFilterType.CUSTOM}" href="#" onclick="showIntervalChooser();"><g:message code="time.filter.show.choose"/></a>
    </div>
    <g:if test="${MeasurementFilterType.valueOf(params.filter) == MeasurementFilterType.CUSTOM}">
        <div id="custom_interval_chooser">
    </g:if>
    <g:else>
        <div id="custom_interval_chooser" style="display:none">
    </g:else>
        <g:form method="GET" mapping='patientGraphs' params='[patientId:session.patientId]'>
            <fieldset class="form">
                <div class="fieldcontain">
                    <label for="start" style="width:auto"><g:message code="time.filter.custom.start"/>:</label>
                    <jq:datePicker name="start" precision="day" value="${params.start}"/>
                    <label for="end" style="width:auto"><g:message code="time.filter.custom.end"/>:</label>
                    <jq:datePicker name="end" precision="day" value="${params.end}"/>
                </div>
                <fieldset class="buttons">
                    <g:hiddenField name="filter" value="${MeasurementFilterType.CUSTOM}"/>
                    <input type="Submit" value="${g.message(code:'time.filter.submit')}" class="acknowledge" style="float:left"/>
                </fieldset>
            </fieldset>
        </g:form>
    </div>

    <!--[if lte IE 7]>
    <i>Der er desværre ikke support for grafer i Internet Explorer 7 eller mindre</i>
    <![endif]-->
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>

    <g:if test="${measurements.size() > 0}">
        <div id="slider"></div>
        <div style="position:relative" id="overlays">
            <div id="marker" style="z-index:10; position: absolute; margin-left: -2px; top: 0; bottom: 0; background: #FF0000; width: 2px">
            </div>
            <div style="z-index: 0;">
                <ol class="property-list">
                    <g:each in="${measurements}" var="measurement">
                        <span class="property-value">
                            <div id="${measurement.type}-${patientInstance.id}"></div>
                        </span>
                        <g:if test="${measurement.type == MeasurementTypeName.BLOODSUGAR.name()}">
                            <span class="property-value">
                                <div id="${MeasurementTypeName.BLOODSUGAR.name() + "-average-day"}-${patientInstance.id}"></div>
                            </span>
                        </g:if>
                        <g:if test="${measurement.type == MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}">
                            <span class="property-value">
                                <div id="${MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name() + "-average-day"}-${patientInstance.id}"></div>
                            </span>
                        </g:if>
                    </g:each>
                </ol>
            </div>
        </div>
    </g:if>
</div>
</body>
</html>
