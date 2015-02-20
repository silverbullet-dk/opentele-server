<%-- Please note: Requires the previous inclusion of _graphFunctions.gsp in the page, as it defines all the --%>
<%-- functionality needed by this file.                                                                     --%>

<%@ page import="grails.converters.JSON; org.opentele.server.model.NumericThreshold; org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.core.model.types.MeasurementFilterType" %>
<g:if test="${measurement.type == MeasurementTypeName.BLOODSUGAR.name()}">
<script type="text/javascript">
 (function() {

     var bloodSugar = window.BloodSugarMeasurement;

     var ticksAsJSON = ${measurement.ticksX as JSON};

     var tickFormatter = function (format, val) {
         return $.jsDate.strftime(val, format);
     };

     var canvasOverlayObjects = [];
     bloodSugar.addTargetIntervalOverlay(canvasOverlayObjects);

     <g:if test="${MeasurementFilterType.valueOf(params.filter) == MeasurementFilterType.CUSTOM} || ${MeasurementFilterType.valueOf(params.filter) == MeasurementFilterType.WEEK}">
     var numberOfTicks = ${measurement.ticksX.size()};
     if (numberOfTicks < 9) {
         tickFormatter = bloodSugar.adjustForNumberOfTicks(canvasOverlayObjects, ticksAsJSON);
     }
     </g:if>

     var series = bloodSugar.handleEmptySeries(${measurement.series as JSON});
     bloodSugar.augmentSeries(series);

     var seriesWithDatesReset = bloodSugar.clone(series);
     seriesWithDatesReset = bloodSugar.handleEmptySeries(bloodSugar.prepareMeasurementSeries(seriesWithDatesReset));

     window.drawGraph({
         containerId: '${measurement.type}-${patient.id}',
         title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.BLOODSUGAR"), patient.name.encodeAsHTML()])}',
         series: series,
         showLegend: true,

         seriesColors: ${measurement.seriesColors as JSON},
         canvasOverlayObjects: canvasOverlayObjects,
         tickFormatter: tickFormatter,
         formatStringX: '${message(code:"graph.label.x.format.BLOODSUGAR")}',
         ticksX: ticksAsJSON,

         minimumY: ${measurement.minY},
         maximumY: ${measurement.maxY},
         labelY: '${message(code:"graph.label.y.BLOODSUGAR")}',
         ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
         alarmValues: ${measurement.alertValues as JSON},
         warningValues: ${measurement.warningValues as JSON},

         <g:if test="${patientIdForFullScreen != null}">
         doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
             document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
         },
         </g:if>
         singleClickFunction: function (ev, seriesIndex, pointIndex, data) {
             window.location.href = '${createLink(controller:"measurement", action:"show")}/' + ${measurement.seriesIds}[seriesIndex][pointIndex];
         }
     }, function (plot) {
         drawStandardGraph(plot);
     });

     var drawStandardGraph = function (plot) {

         var canvasOverlayObjects = [];
         bloodSugar.addTargetIntervalOverlay(canvasOverlayObjects);
         bloodSugar.addTargetIntervalMarkers(canvasOverlayObjects, [
             new Date("Jan 01, 2014 08:00:00").getTime(),
             new Date("Jan 01, 2014 12:00:00").getTime(),
             new Date("Jan 01, 2014 16:00:00").getTime(),
             new Date("Jan 01, 2014 20:00:00").getTime()
         ], 3.0);

         var standardDayContainerId = '${MeasurementTypeName.BLOODSUGAR.name()}-average-day-${patient.id}';
         window.drawGraph({
             containerId: standardDayContainerId,
             title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.BLOODSUGAR_AVERAGE_DAY"), patient.name.encodeAsHTML()])}',
             series: seriesWithDatesReset,
             seriesColors: ${measurement.seriesColors as JSON},
             showLegend: true,

             canvasOverlayObjects: canvasOverlayObjects,
             formatStringX: '${message(code:"graph.label.x.format.BLOODSUGAR_AVERAGE_DAY")}',
             ticksX: bloodSugar.generateTicksX(),

             minimumY: ${measurement.minY},
             maximumY: ${measurement.maxY},
             labelY: '${message(code:"graph.label.y.BLOODSUGAR")}',
             ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
             alarmValues: ${measurement.alertValues as JSON},
             warningValues: ${measurement.warningValues as JSON},

             <g:if test="${patientIdForFullScreen != null}">
             doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                 document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
             },
             </g:if>
             singleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                 window.location.href = '${createLink(controller:"measurement", action:"show")}/' + ${measurement.seriesIds}[seriesIndex][pointIndex];
             },
             highlighterFormatString: bloodSugar.generateHighlighterFormatString(seriesWithDatesReset)
         }, function() {
             bloodSugar.addMouseOverHighlightEvent(standardDayContainerId, plot);
         });

     };
}());
</script>
</g:if>