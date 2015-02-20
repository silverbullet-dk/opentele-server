<%-- Please note: Requires the previous inclusion of _graphFunctions.gsp in the page, as it defines all the --%>
<%-- functionality needed by this file.                                                                     --%>

<%@ page import="grails.converters.JSON; org.opentele.server.model.NumericThreshold; org.opentele.server.core.model.types.MeasurementTypeName" %>
<g:if test="${measurement.type != MeasurementTypeName.BLOODSUGAR.name() && measurement.type != MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}">
    <script type="text/javascript">
        window.drawGraph({
            containerId: '${measurement.type}-${patient.id}',
            title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.${measurement.type}"), patient.name.encodeAsHTML()])}',
            series: ${measurement.series as JSON},
            <g:if test="${!measurement.seriesColors.isEmpty()}">
            seriesColors: ${measurement.seriesColors as JSON},
            showLegend: true,
            </g:if>


            measurementType: '${measurement.type}',
            formatStringX: '${message(code:"graph.label.x.format.${measurement.type}")}',
            ticksX: ${measurement.ticksX as JSON},

            minimumY: ${measurement.minY},
            maximumY: ${measurement.maxY},
            labelY: '${message(code:"graph.label.y.${measurement.type}")}',
            ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
            alarmValues: ${measurement.alertValues as JSON},
            warningValues: ${measurement.warningValues as JSON},

            <g:if test="${patientIdForFullScreen != null}">
            doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
            },
            </g:if>
            singleClickFunction: function(ev, seriesIndex, pointIndex, data) {
                window.location.href = '${createLink(controller:"measurement", action:"show")}/' + ${measurement.ids}[pointIndex];
            }
        });
    </script>
</g:if>
