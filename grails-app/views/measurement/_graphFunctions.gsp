<g:javascript src="jqplot/jquery.jqplot.js"/>
<g:javascript src="jqplot/plugins/jqplot.canvasAxisLabelRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasAxisTickRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasTextRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasOverlay108.js" /> <!-- Force use of newer version canvas overlay with rectangle support -->
<g:javascript src="jqplot/plugins/jqplot.dateAxisRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.cursor.js" />
<g:javascript src="jqplot/plugins/jqplot.highlighter.js" />

<!--[if lt IE 9]>
<g:javascript src="jqplot/excanvas.js"/>
<![endif]-->

<script type="text/javascript">

    // Blood sugar specific

    window.BloodSugarMeasurement = {};

    var handleEmptySeries = function(series) { //JqPlot dislikes empty series, pushing 'null' fixes the problem.
        series.forEach(function(aSeries) {
            if (aSeries.length === 0) {
                aSeries.push(null);
            }
        });
        return series;
    };
    window.BloodSugarMeasurement.handleEmptySeries = handleEmptySeries;

    var prepareMeasurementSeries = function(series) {

        var prepareMeasurement = function (measurement) {
            var date = measurement[0];
            var newDate = new Date(date);
            newDate.setYear(2014);
            newDate.setMonth(0);
            newDate.setDate(1);
            measurement[0] = newDate.getTime();
        };

        series.forEach(function(measurements) {
            if (measurements !== null) {
                measurements.forEach(function(measurement) {
                    if (measurement !== null) {
                        prepareMeasurement(measurement);
                    }
                });
            }
        });

        return series;
    };
    window.BloodSugarMeasurement.prepareMeasurementSeries = prepareMeasurementSeries;

    var augmentSeries = function(series) {

        var copyDate = function(measurement) {
            var formatDate = function (date) {
                return "" + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
            };

            measurement[5] = formatDate(new Date(measurement[0]));
        };

        var handleMeasurementNote = function(measurement) {
            var ackNote = measurement[3];
            if (ackNote !== '' && measurement.length > 3) {
                measurement[3] = '<tr>' +
                        '<td>${message(code:'patient.acknowledge.note')}:</td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td nowrap="wrap" style="width:160px;">' + ackNote + '</td>' +
                        '</tr>';
            }
        };

        var id = 0;
        series.forEach(function(measurements) {
            measurements.forEach(function(measurement) {
                if (measurement === null) {
                    return;
                }
                copyDate(measurement);
                handleMeasurementNote(measurement);
                measurement.push(id);
                id++;
            });
        });
    };
    window.BloodSugarMeasurement.augmentSeries = augmentSeries;

    var clone = function(obj) {
        return JSON.parse(JSON.stringify(obj));
    };
    window.BloodSugarMeasurement.clone = clone;

    var generateHighlighterFormatString = function(series) {
        var seenAckNote = false;
        series.forEach(function(aSeries) {
            aSeries.some(function(aMeasurement) {
                if (aMeasurement !== undefined && aMeasurement !== null) {
                    var ackNote = aMeasurement[3];
                    if (ackNote !== undefined && ackNote !== null && ackNote !== '') {
                        seenAckNote = true;
                    }
                }
            });
        });
        if (seenAckNote) {
            return '<table><tr><td>%6$s %3$s, %5$s</td></tr>%4$s</table>';
        } else {
            return '<div>%6$s %3$s, %5$s</div>';
        }
    };
    window.BloodSugarMeasurement.generateHighlighterFormatString = generateHighlighterFormatString;

    var addMouseOverHighlightEvent = function (containerId, plot) {

        var highlightPoint = function (plot, seriesIndex, pointIndex) {

            var hexToRgb = function (hex, opacity) {
                var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
                return 'rgba(' +
                        parseInt(result[1], 16) + ',' +
                        parseInt(result[2], 16) + ',' +
                        parseInt(result[3], 16) + ',' +
                        opacity + ')';
            };

            var series = plot.series[seriesIndex];
            if (series.data[pointIndex] === undefined ||
                    series.data[pointIndex] === undefined) {
                return;
            }
            var x = plot.axes.xaxis.series_u2p(series.data[pointIndex][0]);
            var y = plot.axes.yaxis.series_u2p(series.data[pointIndex][1]);
            var color = hexToRgb(series.color, 0.6);
            var radius = 12;
            var end = 2 * Math.PI;
            var bloodSugarGraph = $(plot.targetId);
            var drawingCanvas = bloodSugarGraph.find('.jqplot-highlight-canvas')[0];
            var context = drawingCanvas.getContext('2d');
            context.clearRect(0, 0, drawingCanvas.width, drawingCanvas.height);
            context.strokeStyle = 'rgba(0,0,0,0)';
            context.fillStyle = color;
            context.beginPath();
            context.arc(x, y, radius, 0, end, true);
            context.closePath();
            context.stroke();
            context.fill();
        };

        var findCorrespondingPointIndex = function(plot, seriesIndex, point) {
            var idIdx = 6;
            var standardDayPointId = point[idIdx];
            var series = plot.series[seriesIndex].data;
            var counter = 0;
            series.some(function(point) {
                var pointId = point[idIdx];
                if (pointId === standardDayPointId) {
                    return true;
                }
                ++counter;
            });
            return counter;
        };

        var MOUSE_OVER_EVENT = 'jqplotDataMouseOver';
        var container = $('#' + containerId);
        container.bind(MOUSE_OVER_EVENT, function (evt, seriesIndex, pointIndex, data) {
            if (plot.series[seriesIndex] !== undefined && plot.series[seriesIndex] !== null) {
                var otherPointIndex = findCorrespondingPointIndex(plot, seriesIndex, data);
                highlightPoint(plot, seriesIndex, otherPointIndex);
            }
        });
    };
    window.BloodSugarMeasurement.addMouseOverHighlightEvent = addMouseOverHighlightEvent;

    var addTargetIntervalOverlay = function(canvasOverlayObjects) {
        <g:if test="${!request.graphForPatient}"> //Do not show target areas to patients
            canvasOverlayObjects.push({rectangle: {
                ymin: 4,
                ymax: 10,
                xminOffset: "0px",
                xmaxOffset: "0px",
                yminOffset: "0px",
                ymaxOffset: "0px",
                color: "rgba(211, 211, 211, 0.45)"
            }});
        </g:if>
    };
    window.BloodSugarMeasurement.addTargetIntervalOverlay = addTargetIntervalOverlay;

    var addTargetIntervalMarkers = function(canvasOverlayObjects, timeMarkers, lineWidth) {
        timeMarkers.forEach(function(timeMarker) {
            canvasOverlayObjects.push({verticalLine: {
                x: timeMarker,
                lineWidth: lineWidth,
                dashPattern: [2, 4],
                color: 'rgb(66, 98, 144)',
                shadow: false
            }});
        });
    };
    window.BloodSugarMeasurement.addTargetIntervalMarkers = addTargetIntervalMarkers;

    var adjustForNumberOfTicks = function(canvasOverlayObjects, ticksAsJSON) {

        for (var index = 0; index < ticksAsJSON.length - 1; index = index + 4) {

            var tick = ticksAsJSON[index];
            var tick1 = new Date(tick);
            var tick2 = new Date(tick);
            var tick3 = new Date(tick);
            tick1.setHours(6);
            tick1.setSeconds(1); // Hack to differentiate this date as a custom minor tick
            tick2.setHours(12);
            tick2.setSeconds(1);
            tick3.setHours(18);
            tick3.setSeconds(1);

            ticksAsJSON.splice(index + 1, 0, tick1);
            ticksAsJSON.splice(index + 2, 0, tick2);
            ticksAsJSON.splice(index + 3, 0, tick3);

            addTargetIntervalMarkers(canvasOverlayObjects, [
                new Date(tick1).getTime(),
                new Date(tick2).getTime(),
                new Date(tick3).getTime()
            ], 0.5);

        }

        // Override setTick function to hide gridlines not at zero seconds by setting them to minorTick
        $.jqplot.CanvasAxisTickRenderer.prototype.setTick = function (value, axisName, isMinor) {
            this.value = value;
            var testDate = new Date(value);
            this.isMinorTick = testDate.getSeconds() != 0;
            return this;
        };

        // Return a custom Tickformatter that dynamically switches formatter if ticks are not at zero seconds
        return function (format, val) {
            var testDate = new Date(val);
            if (testDate.getSeconds() === 0) {
                return $.jsDate.strftime(val, format);
            } else {
                return $.jsDate.strftime(val, '%H:%M');
            }
        };

    };
    window.BloodSugarMeasurement.adjustForNumberOfTicks = adjustForNumberOfTicks;

    var generateTicksX = function() {
        var dates = [];
        for (var i = 0; i < 24; i++) {
            if (i < 10) {
                dates.push(new Date("Jan 01, 2014 0" + i + ":00:00"));
            } else {
                dates.push(new Date("Jan 01, 2014 " + i + ":00:00"));
            }
        }
        dates.push(new Date("Jan 01, 2014 23:59:59"));
        return dates;
    };
    window.BloodSugarMeasurement.generateTicksX = generateTicksX;

    // General

    window.drawGraph = function(options, callback) {
        $(function() {

            $.jqplot.postDrawHooks.push(function() {
                $(".jqplot-overlayCanvas-canvas").css('z-index', '0'); //send overlay canvas to back
                $(".jqplot-series-canvas").css('z-index', '1'); //send series canvas to front
                $(".jqplot-table-legend").css('z-index', '2'); //send legend to front
                $(".jqplot-highlighter-tooltip").css('z-index', '3'); //make sure the tooltip is over the series
                $(".jqplot-event-canvas").css('z-index', '5'); //must be on the very top since it is responsible for event catching and propagation
            });

            var containerId = options['containerId'];
            var title = options['title'];
            var series = options['series'];

            var canvasOverlayObjects = options['canvasOverlayObjects'];
            if (canvasOverlayObjects == undefined) {
                canvasOverlayObjects = Array();
            }

            var tickFormatter = options['tickFormatter'];
            var formatStringX = options['formatStringX'];
            var ticksX = options['ticksX'];

            var minimumY = options['minimumY'];
            var maximumY = options['maximumY'];
            var labelY = options['labelY'];
            var ticksY = options['ticksY'];

            <g:if test="${request.showThresholds}">
                var alarmValues = options['alarmValues'];
                var warningValues = options['warningValues'];
            </g:if>
            <g:else>
                var alarmValues = [];
                var warningValues = [];
            </g:else>

            var measurementType = options['measurementType'];
            var seriesColors = options['seriesColors'];
            var highlighterFormatString = options['highlighterFormatString'];

            var singleClickFunction = options['singleClickFunction'];
            var doubleClickFunction = options['doubleClickFunction'];

            var container = $('#' + containerId);
            var showLegend = options['showLegend'];
            <g:if test="${request.showThresholds}">
                container.bind('jqplotDataClick', singleClickFunction);
                if (doubleClickFunction) {
                    container.bind('jqplotDblClick', doubleClickFunction);
                }
            </g:if>
            <g:if test="${request.showThresholds} || ${request.showHighlighterFormatString}">
            if (!highlighterFormatString) {
                <g:if test="${request.graphForPatient}">
                highlighterFormatString = '<div>%1$s %3$s, %5$s</div>';
                </g:if>
                <g:else>
                var seenAckNote = false;
                series.forEach(function(aSeries) {
                    aSeries.forEach(function(aMeasurement) {
                        if (aMeasurement !== undefined && aMeasurement !== null) {
                            var ackNote = aMeasurement[3];
                            if (ackNote !== undefined && ackNote !== null && ackNote !== '') {
                                seenAckNote = true;
                                aMeasurement[3] =
                                        '<tr>' +
                                          '<td>' +
                                            '${message(code:'patient.acknowledge.note')}:' +
                                          '</td>' +
                                        '</tr>' +
                                        '<tr>' +
                                          '<td nowrap="wrap">' +
                                            ackNote +
                                          '</td>' +
                                        '</tr>';
                            }
                        }
                    });
                });
                if (seenAckNote) {
                    highlighterFormatString = '<table><tr><td>%1$s %3$s, %5$s</td></tr>%4$s</table>';
                } else {
                    highlighterFormatString = '<div>%1$s %3$s, %5$s</div>';
                }
                </g:else>
            }
            </g:if>
            <g:else>
                highlighterFormatString = '<div>%5$s</div>';
            </g:else>

            var seriesConfig;
            switch(measurementType) {
                case 'BLOOD_PRESSURE':
                    if (seriesColors) {
                        seriesConfig = [];
                        for (var i = 0; i < seriesColors.length; i++) {
                            seriesConfig[i] = {
                                showLine: true,
                                color: seriesColors[i],
                                markerOptions: {size: 5}
                            };

                            seriesConfig[2] = { // Third series is for heart rate (pulse) in blood pressure graph
                                showLine: false,
                                color: seriesColors[i],
                                markerOptions: { style: "x", size: 7}
                            };
                        }

                        seriesConfig[0].label = "${message(code: 'patient.overview.measurements.SYSTOLIC_BLOOD_PRESSURE')}";
                        seriesConfig[1].label = "${message(code: 'patient.overview.measurements.DIASTOLIC_BLOOD_PRESSURE')}";
                        seriesConfig[2].label = "${message(code: 'patient.overview.table.label.PULSE')}";
                    }
                    break;
                case 'SYSTOLIC_BLOOD_PRESSURE':
                    if (seriesColors) {
                        seriesConfig = [{
                            showLine: true,
                            color: seriesColors[0],
                            markerOptions: {size: 5}
                        }];

                        seriesConfig[0].label = "${message(code: 'patient.overview.measurements.SYSTOLIC_BLOOD_PRESSURE')}";
                    }

                    // Mark target systolic interval with a lightgray color
                    <g:if test="${!request.graphForPatient}">
                    canvasOverlayObjects.push({rectangle: {
                        ymin: 90,
                        ymax: 130,
                        xminOffset: "0px",
                        xmaxOffset: "0px",
                        yminOffset: "0px",
                        ymaxOffset: "0px",
                        color: "rgba(211, 211, 211, 0.45)"
                    }});
                    </g:if>

                    break;
                case 'DIASTOLIC_BLOOD_PRESSURE':
                    if (seriesColors) {
                        seriesConfig = [{
                            showLine: true,
                            color: seriesColors[0],
                            markerOptions: {size: 5}
                        }];

                        seriesConfig[0].label = "${message(code: 'patient.overview.measurements.DIASTOLIC_BLOOD_PRESSURE')}";
                    }

                    // Mark target diastolic interval with a lightgray color
                    <g:if test="${!request.graphForPatient}">
                    canvasOverlayObjects.push({rectangle: {
                        ymin: 60,
                        ymax: 80,
                        xminOffset: "0px",
                        xmaxOffset: "0px",
                        yminOffset: "0px",
                        ymaxOffset: "0px",
                        color: "rgba(211, 211, 211, 0.45)"
                    }});
                    </g:if>

                    break;
                case 'PULSE':
                    if (seriesColors) {
                        seriesConfig = [{
                            showLine: true,
                            color: seriesColors[0],
                            markerOptions: { style: "x", size: 7}
                        }];

                        seriesConfig[0].label = "${message(code: 'patient.overview.table.label.PULSE')}";
                    }

                    <g:if test="${!request.graphForPatient}">
                    // Mark target pulse interval with a lightgray color
                    canvasOverlayObjects.push({rectangle: {
                        ymin: 60,
                        ymax: 100,
                        xminOffset: "0px",
                        xmaxOffset: "0px",
                        yminOffset: "0px",
                        ymaxOffset: "0px",
                        color: "rgba(211, 211, 211, 0.45)"
                    }});
                    </g:if>

                    break;
                case 'CONTINUOUS_BLOOD_SUGAR_MEASUREMENT':
                    seriesConfig = [
                        {
                            label: "${message(code: 'graph.cgm.legend.continuousBloodSugarMeasurementSeries.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#47acc0"
                        },
                        {
                            label: "${message(code: 'graph.cgm.legend.coulometerReadingEventSeries.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#FFFF79"
                        },

                        {
                            label: "${message(code: 'graph.cgm.legend.insulinEventSeries.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#45C545"
                        },
                        {
                            label: "${message(code: 'graph.cgm.legend.exerciseEventSeries.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#5c5c5e"
                        },
                        {
                            label: "${message(code: 'graph.cgm.legend.stateOfHealthEvent.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color:"#8b2671"
                        },
                        {
                            label: "${message(code: 'graph.cgm.legend.mealEvent.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#fc4c0a"
                        },
                        {
                            label: "${message(code: 'graph.cgm.legend.genericEvent.legend')}",
                            showLine: false,
                            markerOptions: {size: 5},
                            color: "#0053f9"
                        }
                    ];
                    break;
                default:
                    if (seriesColors) {
                        seriesConfig = [];
                        for (var i = 0; i < seriesColors.length; i++) {
                            seriesConfig[i] = {
                                showLine: false,
                                color: seriesColors[i],
                                markerOptions: {size: 5}
                            }
                        }

                        seriesConfig[0].label = "${message(code: 'patient.overview.bloodsugar.beforemeal')}";
                        if (seriesColors.length > 1) {
                            seriesConfig[1].label = "${message(code: 'patient.overview.bloodsugar.aftermeal')}";
                        }
                        if (seriesColors.length > 2) {
                            seriesConfig[2].label = "${message(code: 'patient.overview.bloodsugar.unknown')}";
                        }
                    }
                    break;
            }

            var graph = $.jqplot(containerId, series, {
                title: title,
                gridPadding: { left: 40 },
                series: seriesConfig,

                axes: {
                    xaxis: {
                        pad: 1.2,
                        renderer: $.jqplot.DateAxisRenderer,
                        rendererOptions: {
                            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                            tickOptions: {
                                formatter: tickFormatter
                            }
                        },
                        ticks: ticksX,
                        tickOptions: {
                            formatString: formatStringX,
                            angle: -45
                        }
                    },
                    yaxis: {
                        min: minimumY,
                        max: maximumY,
                        label: labelY,
                        labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                        ticks: ticksY
                    }
                },
                canvasOverlay: {
                    show: true,
                    objects: canvasOverlayObjects
                },
                legend:{
                    renderer: $.jqplot.EnhancedLegendRenderer,
                    show: showLegend,
                    location: 'nw'
                },
                highlighter: {
                    show: true,
                    sizeAdjust: 7.5,
                    tooltipOffset: 3,
                    fadeTooltip:true,
                    tooltipLocation: "sw",
                    yvalues: 5,
                    formatString: highlighterFormatString
                },
                cursor: {
                    show: false
                }
            });

            function drawLine(color, y) {
                var canvas = $('#' + containerId + '>.jqplot-series-canvas')[0];
                if (canvas) {
                    var context = canvas.getContext("2d");
                    context.save();
                    context.strokeStyle = color;

                    var distanceFromLowerEdge = y - minimumY;
                    var canvasY = canvas.height - (distanceFromLowerEdge * canvas.height)/(maximumY - minimumY);
                    context.beginPath();
                    context.moveTo(0, canvasY);
                    context.lineTo(canvas.width, canvasY);
                    context.closePath();
                    context.stroke();

                    context.restore();
                }
            }

            function drawThresholdIndicators() {
                var i;
                for (i = 0; i<alarmValues.length; i++) {
                    drawLine('red', alarmValues[i]);
                }
                for (i = 0; i<warningValues.length; i++) {
                    drawLine('yellow', warningValues[i]);
                }
            }
            // A bit hacky... if the div for the graph has style "display:none" when the graph is defined, JQPlot
            // does not plot the diagram. When the diagram is later shown, the code showing the graph must then
            // "trigger" the 'visibilityChanged' event to allow us to replot the graph.
            container.bind('visibilityChanged', function() {
                graph.replot();
                drawThresholdIndicators();
            });

            drawThresholdIndicators();

            if (callback !== undefined && callback !== null) {
                callback(graph);
            }
        });
    }
</script>
