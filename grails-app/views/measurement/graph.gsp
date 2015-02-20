<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="fullScreen">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/>  ${patient.name.encodeAsHTML()}</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.jqplot.css')}" type="text/css">
    <g:javascript src="jquery.js"/>
    <g:set var="showHighlighterFormatString" value="${true}" scope="request"/>
    <g:render template="graphFunctions"/>

    <g:if test="${measurement.type == MeasurementTypeName.BLOODSUGAR.name()}">
        <g:render template="bloodSugarMeasurementGraphs" model='[patient: patient, measurement: measurement]'/>
    </g:if>
    <g:elseif test="${measurement.type == MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}">
        <g:render template="continuousBloodSugarMeasurementGraphs" model='[patient: patient, measurement: measurement]'/>
    </g:elseif>
    <g:else>
        <g:render template="measurementGraph" model='[patient: patient, measurement: measurement, title: message(code: "patient.graph.of",args: [message(code: "graph.legend.${measurement.type}"), patient.name.encodeAsHTML()])]'/>
    </g:else>
    <style type="text/css">
   		body { height: 100% }
   		.front { position: absolute; z-index: 20 }
        .fullScreen { position: absolute; left: 0; right: 0; top: 0; bottom: 0; }
   		.fullScreenGraph { position: absolute; left: 3em; right: 0; bottom: 0; height:100%; }
        .halfScreen { left: 3em; right: 0; height:50%; }
	</style>
</head>

<body>
	<div class="front">
		<a href='${createLink(mapping: "patientGraphs", params: [patientId: patient.id])}'><g:message code="default.back"/></a>
	</div>
	<div class="content scaffold-show fullScreen" role="main">
	    <!--[if lte IE 7]>
		<i>Der er desværre ikke support for grafer i Internet Explorer 7 eller mindre</i>
		<![endif]-->
        <![if gt IE 7]>
        <g:if test="${measurement.type == MeasurementTypeName.BLOODSUGAR.name()}">
            <div id="${MeasurementTypeName.BLOODSUGAR.name()}-${patient.id}" class="halfScreen"></div>
            <div id="${MeasurementTypeName.BLOODSUGAR.name()}-average-day-${patient.id}" class="halfScreen"></div>
        </g:if>
        <g:elseif test="${measurement.type == MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}">
            <div id="${MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}-${patient.id}" class="halfScreen"></div>
            <div id="${MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}-average-day-${patient.id}" class="halfScreen"></div>
        </g:elseif>
        <g:else>
            <div id="${measurement.type}-${patient.id}" class="fullScreenGraph"></div>
        </g:else>
        <![endif]>
	</div>
</body>
</html>
