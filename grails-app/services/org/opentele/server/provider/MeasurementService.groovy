package org.opentele.server.provider

import groovy.transform.EqualsAndHashCode
import org.apache.commons.lang.StringUtils
import org.opentele.server.provider.graph.YAxisTickCalculator
import org.opentele.server.model.*
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.provider.util.AboveAlertThresholdPredicate
import org.opentele.server.provider.util.AboveWarningThresholdPredicate
import org.opentele.server.core.util.TimeFilter
import org.opentele.server.model.cgm.*

import java.text.DateFormat
import java.text.SimpleDateFormat

class TableMeasurement {
    private org.opentele.server.model.Measurement measurement
    private boolean warning, alert

    TableMeasurement(org.opentele.server.model.Measurement measurement, boolean warning, boolean alert) {
        this.measurement = measurement
        this.warning = warning
        this.alert = alert
    }

    def time() { measurement.time }

    def unit() { measurement.unit }

    def meterModel() { measurement.meter?.model }

    def value() { measurement.toString() }

    def warning() { !alert && warning }

    def alert() { alert }

    def exportedToKih() { measurement.exportedToKih }

    def shouldBeExportedToKih() { measurement.shouldBeExportedToKih() }

    def conference() { measurement.conference }

    def consultation() { measurement.consultation }
}

class TickY {
    double value
    String text
}

class TableData {
    MeasurementTypeName type
    List<TableMeasurement> measurements
}

class GraphData {
    String type
    List<Double> alertValues, warningValues
    long start, end
    double minY, maxY
    List<Long> ids
    List<List<Long>> seriesIds
    List<List> series
    List<String> ticksX = []
    List<TickY> ticksY
    List<String> seriesColors = []
}

class BloodsugarDataPerDate {
    Calendar date
    List<BloodsugarDataMeasurement> zeroToFour
    List<BloodsugarDataMeasurement> fiveToTen
    List<BloodsugarDataMeasurement> elevenToSixteen
    List<BloodsugarDataMeasurement> seventeenToTwentyThree
}

@EqualsAndHashCode
class BloodsugarDataMeasurement {
    Measurement measurement
    //Double value
    Calendar timestamp
    boolean beforeMeal
    boolean afterMeal
    boolean controlMeasurement
    def otherInformation

    def value() {
        measurement.toString()
    }

    @Override
    def String toString() {
        "${value()},${timestamp.format("dd/MM/yyyy HH:mm")},${beforeMeal},${afterMeal}"
    }
}

class MeasurementService {
    // SimpleDateFormat is not thread-safe, so we need to wrap it in a ThreadLocal
    private ThreadLocal<DateFormat> hourAndMinuteDateFormatter = new ThreadLocal<DateFormat>() {
        DateFormat get() { return new SimpleDateFormat("HH:mm") }
    }
    private ThreadLocal<DateFormat> yearMonthDayDateFormatter = new ThreadLocal<DateFormat>() {
        DateFormat get() { return new SimpleDateFormat("yyyy-MM-dd") }
    }

    def i18nService

    def dataForGraphsAndTables(Patient patient, TimeFilter timeFilter) {
        def graphData = []
        def tableData = []
        def bloodsugarData = []

        MeasurementType.list().each { type ->
            processMeasurements(patient, timeFilter, type) { List<Measurement> measurementsOfType ->
                addTableData(type, measurementsOfType, tableData)
                addBloodsugarData(type, measurementsOfType, bloodsugarData)
                addGraphData(patient, type, timeFilter, measurementsOfType, graphData)
            }
        }
        adjustStartAndEndTimes(graphData)

        [graphData, tableData, bloodsugarData]
    }

    // Client + server
    def dataForTablesAndBloodsugar(Patient patient, TimeFilter timeFilter) {
        def tableData = []
        def bloodsugarData = []

        MeasurementType.list().each { type ->
            processMeasurements(patient, timeFilter, type) { List<Measurement> measurementsOfType ->
                addTableData(type, measurementsOfType, tableData)
                addBloodsugarData(type, measurementsOfType, bloodsugarData)
            }
        }

        [tableData, bloodsugarData]
    }

    // Only client
    def dataForTables(Patient patient, TimeFilter timeFilter) {
        def tableData = []

        MeasurementType.list().each { type ->
            processMeasurements(patient, timeFilter, type) { List<Measurement> measurementsOfType ->
                addTableData(type, measurementsOfType, tableData)
            }
        }

        tableData
    }

    List<GraphData> dataForGraphs(Patient patient, TimeFilter timeFilter) {
        def graphData = []

        MeasurementType.list().each { type ->
            processMeasurements(patient, timeFilter, type) { List<Measurement> measurementsOfType ->
                addGraphData(patient, type, timeFilter, measurementsOfType, graphData)
            }
        }
        adjustStartAndEndTimes(graphData)

        graphData
    }

    // Client + provider
    GraphData dataForGraph(Patient patient, TimeFilter timeFilter, String measurementTypeName) {
        def graphData = []

        def measurementType = resolveMeasurementType(measurementTypeName)
        processMeasurements(patient, timeFilter, measurementType) { List<Measurement> measurementsOfType ->
            addGraphData(patient, measurementType, timeFilter, measurementsOfType, graphData)
        }

        adjustStartAndEndTimes(graphData)

        graphData.find { it.type == measurementTypeName }
    }

    private MeasurementType resolveMeasurementType(String type) {
        MeasurementTypeName measurementTypeName
        switch(type) {
            case ['SYSTOLIC_BLOOD_PRESSURE','DIASTOLIC_BLOOD_PRESSURE']:
                measurementTypeName = MeasurementTypeName.BLOOD_PRESSURE
                break;
            default:
                measurementTypeName = MeasurementTypeName.safeValueOf(type)

        }
        MeasurementType.findByName(measurementTypeName)
    }

    List<BloodsugarDataPerDate> dataForBloodsugar(Patient patient, TimeFilter timeFilter) {
        def bloodsugarData = []

        MeasurementType.list().each { type ->
            processMeasurements(patient, timeFilter, type) { List<Measurement> measurementsOfType ->
                addBloodsugarData(type, measurementsOfType, bloodsugarData)
            }
        }

        bloodsugarData
    }

    private void processMeasurements(Patient patient, TimeFilter timeFilter, MeasurementType type, Closure closure) {
        List<Measurement> measurementsOfType = findMeasurements(patient, type, timeFilter)
        if (measurementsOfType) {
            closure.call(measurementsOfType)
        }
    }

    private void addGraphData(Patient patient, MeasurementType type, TimeFilter timeFilter, List<Measurement> measurementsOfType, List graphDataList) {
        if(!(type.name in MeasurementTypeName.TABLE_CAPABLE_MEASUREMENT_TYPE_NAME)) {
            return
        }

        def validMeasurements = measurementsOfType.findAll { !it.isIgnored() }
        if (!validMeasurements) {
            return
        }

        switch (type.name) {
            case MeasurementTypeName.BLOOD_PRESSURE:
                def heartRateGraphData = dataForGraph(patient, timeFilter, MeasurementTypeName.PULSE.toString())

                graphDataList << createBloodPressureGraphData(validMeasurements, timeFilter, heartRateGraphData)
                if(patient.getThreshold(MeasurementTypeName.BLOOD_PRESSURE)) {
                    graphDataList << createSystolicGraphData(patient, validMeasurements, timeFilter)
                    graphDataList << createDiastolicGraphData(patient, validMeasurements, timeFilter)
                }
                return
            case MeasurementTypeName.PULSE:
                graphDataList << createPulseGraphData(patient, validMeasurements, timeFilter)
                return
            case MeasurementTypeName.BLOODSUGAR:
                graphDataList << createBloodsugarGraphData(patient, validMeasurements, timeFilter)
                return
            case MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT:
                graphDataList << createContinuousBloodGlucoseGraphData(patient, validMeasurements, timeFilter)
                return
            default:
                graphDataList << createGraphData(patient, type.name, validMeasurements, timeFilter)
                return

        }
    }

    private void addTableData(MeasurementType type, List<Measurement> measurementsOfType, List tableDataList) {
        tableDataList << createTableData(type.name, measurementsOfType)
    }

    private void addBloodsugarData(MeasurementType type, List<Measurement> measurementsOfType, List bloodsugarDataList) {
        if (type.name == MeasurementTypeName.BLOODSUGAR) {
            bloodsugarDataList.addAll(createBloodsugarData(measurementsOfType))
        }
    }


    private void adjustStartAndEndTimes(List<GraphData> graphData) {
        if (graphData) {
            def start = graphData.collect { it.start }.min()
            def end = graphData.collect { it.end }.max()
            def ticksX = ticks(start, end)

            graphData*.start = start
            graphData*.end = end
            graphData*.ticksX = ticksX
        }
    }

    private def ticks(long start, long end) {
        int numberOfDays = daysBetween(start, end)
        int tickInterval = numberOfDays <= 32 ? 1 : Math.ceil(numberOfDays / 20)

        def result = []
        def roller = Calendar.getInstance()
        roller.setTimeInMillis(end)
        while (roller.getTimeInMillis() > start) {
            result << yearMonthDayDateFormatter.get().format(roller.getTime())
            roller.add(Calendar.DATE, -tickInterval)
        }
        roller.setTimeInMillis(start)
        result << yearMonthDayDateFormatter.get().format(roller.getTime())
        result.reverse()
    }

    private int daysBetween(long start, long end) {
        Calendar roller = Calendar.getInstance()
        roller.setTimeInMillis(start)
        int result = 0
        while (roller.getTimeInMillis() < end) {
            roller.add(Calendar.DATE, 1)
            result++
        }
        result
    }


    // Client + server
    private TableData createTableData(MeasurementTypeName type, List<Measurement> measurements) {
        def tableMeasurementsNewestFirst = measurements.collect { measurement ->
            new TableMeasurement(measurement, AboveWarningThresholdPredicate.isTrueFor(measurement), AboveAlertThresholdPredicate.isTrueFor(measurement))
        }.reverse()

        new TableData(type: type, measurements: tableMeasurementsNewestFirst)
    }

    private List<BloodsugarDataPerDate> createBloodsugarData(List<Measurement> measurements) {
        def bloodsugarMeasurements = measurements.collect { it ->

            new BloodsugarDataMeasurement(
                    measurement: it,
                    timestamp: it.time.toCalendar(),
                    beforeMeal: it.isBeforeMeal ?: false,
                    afterMeal: it.isAfterMeal ?: false,
                    controlMeasurement: it.isControlMeasurement ?: false,
                    otherInformation: it.otherInformation ?: false
            )
        }

        def byDate = bloodsugarMeasurements.
                groupBy { it.timestamp.format("yyyy/MM/dd") }.
                sort { it.key }

        def dates = byDate.collect { dateString, measurementsOnDate ->
            def date = new Date().parse("yyyy/MM/dd", dateString)

            def byPeriod = measurementsOnDate.groupBy { measurement ->
                def hour = measurement.timestamp[Calendar.HOUR_OF_DAY]
                switch (hour) {
                    case 0..4: return 0         // Period 00.00 - 04.59
                    case 5..10: return 1        // Period 00.05 - 10.59
                    case 11..16: return 2       // Period 11.00 - 16.59
                    case 17..23: return 3       // Period 17.00 - 23.59
                }
            }

            // Sort each period so that the measurements are chronological.
            for (kv in byPeriod) {
                kv.value.sort { it.timestamp }
            }

            new BloodsugarDataPerDate(date: date.toCalendar(),
                    zeroToFour: byPeriod[0] ?: [],
                    fiveToTen: byPeriod[1] ?: [],
                    elevenToSixteen: byPeriod[2] ?: [],
                    seventeenToTwentyThree: byPeriod[3] ?: []
            )
        }

        dates
    }

    private def createGraphData(Patient patient, MeasurementTypeName type, List<Measurement> measurements, TimeFilter timeFilter) {
        def series = generalGraphSeries(measurements)
        def (alertValues, warningValues) = findThresholdValues(type, patient)
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        def ticksY = YAxisTickCalculator.calculate(type, minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: type.toString(),
                alertValues: alertValues, warningValues: warningValues,
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                series: series)
    }

    private def createBloodPressureGraphData(List<Measurement> measurements, TimeFilter timeFilter, GraphData heartRateGraphData = null) {
        def series = bloodPressureSeries(measurements)
        def (alertValues, warningValues) = [[], []]
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        if(heartRateGraphData?.getSeries()?.first()) {
            series << heartRateGraphData.series.first()
            minY = Math.min(minY, heartRateGraphData.minY)
            maxY = Math.max(maxY, heartRateGraphData.maxY)
        }

        def ticksY = YAxisTickCalculator.calculate(MeasurementTypeName.BLOOD_PRESSURE, minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: 'BLOOD_PRESSURE',
                alertValues: [], warningValues: [],
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                series: series,
                seriesColors: ['#46b2c7', '#eca306', '#666668'])
    }

    private def createContinuousBloodGlucoseGraphData(Patient patient, List<Measurement> measurements, TimeFilter timeFilter) {
        def series = continuousBloodGlucoseGraphSeries(measurements, timeFilter)

        def (alertValues, warningValues) = [[], []]
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def cgmMeasurements = measurements*.continuousBloodSugarEvents.flatten()
        def timestamps = cgmMeasurements.collect { it.time }
        def (start, end) = calculateStartAndEndFromDates(timestamps, timeFilter)

        def ticksY = YAxisTickCalculator.calculate(MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT, minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.toString(),
                alertValues: [], warningValues: [],
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: null,
                series: series)
    }

    private def createPulseGraphData(Patient patient, List<Measurement> measurements, TimeFilter timeFilter) {
        def series = pulseGraphSeries(measurements)
        def (alertValues, warningValues) = findThresholdValues(MeasurementTypeName.PULSE, patient)
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        def ticksY = YAxisTickCalculator.calculate(MeasurementTypeName.PULSE, minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: MeasurementTypeName.PULSE.toString(),
                alertValues: alertValues, warningValues: warningValues,
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                series: series,
                seriesColors: ['#666668'])
    }

    private def createBloodsugarGraphData(Patient patient, List<Measurement> measurements, TimeFilter timeFilter) {
        def beforeMealMeasurements = measurements.findAll {it.isBeforeMeal}
        def beforeMealSeries = generalGraphSeries(beforeMealMeasurements).first()

        def afterMealMeasurements = measurements.findAll {it.isAfterMeal}
        def afterMealSeries = generalGraphSeries(afterMealMeasurements).first()

        def unknownMealRelationMeasurements = measurements.findAll{ !(it.isAfterMeal || it.isBeforeMeal) }
        def unknownMealRelationSeries = generalGraphSeries(unknownMealRelationMeasurements).first()



        def series = [beforeMealSeries, afterMealSeries, unknownMealRelationSeries]
        def (alertValues, warningValues) = findThresholdValues(MeasurementTypeName.BLOODSUGAR, patient)
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        def seriesIds = [beforeMealMeasurements*.id, afterMealMeasurements*.id, unknownMealRelationMeasurements*.id]
        def ticksY = YAxisTickCalculator.calculate(MeasurementTypeName.BLOODSUGAR, minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: MeasurementTypeName.BLOODSUGAR.toString(),
                alertValues: alertValues, warningValues: warningValues,
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                seriesIds: seriesIds,
                series: series,
                seriesColors: ['#fc494d', '#345dff', '#666668'])
    }

    private GraphData createSystolicGraphData(Patient patient, List<Measurement> measurements, TimeFilter timeFilter) {
        def series = systolicGraphSeries(measurements)
        def (alertValues, warningValues) = systolicThresholdValues(patient.getThreshold(MeasurementTypeName.BLOOD_PRESSURE) as BloodPressureThreshold)
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        def ticksY = YAxisTickCalculator.calculateSystolic(minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: 'SYSTOLIC_BLOOD_PRESSURE',
                alertValues: alertValues, warningValues: warningValues,
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                series: series,
                seriesColors: ['#46b2c7'])
    }

    private GraphData createDiastolicGraphData(Patient patient, List<Measurement> measurements, TimeFilter timeFilter) {
        def series = diastolicGraphSeries(measurements)
        def (alertValues, warningValues) = diastolicThresholdValues(patient.getThreshold(MeasurementTypeName.BLOOD_PRESSURE) as BloodPressureThreshold)
        def (double minY, double maxY) = calculateMinAndMaxY(series, alertValues, warningValues)
        def (start, end) = calculateStartAndEnd(measurements, timeFilter)
        def ids = measurements*.id
        def ticksY = YAxisTickCalculator.calculateDiastolic(minY, maxY)
        (minY, maxY) = [ticksY.first().value, ticksY.last().value]

        //noinspection GroovyAssignabilityCheck
        new GraphData(type: 'DIASTOLIC_BLOOD_PRESSURE',
                alertValues: alertValues, warningValues: warningValues,
                start: start, end: end,
                minY: minY, maxY: maxY,
                ticksY: ticksY,
                ids: ids,
                series: series,
                seriesColors: ['#eca306'])
    }

    private List<Measurement> findMeasurements(Patient patient, MeasurementType type, TimeFilter timeFilter) {

        if(type.name == MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT) {
            if(timeFilter.isLimited) {
                def measurements = Measurement.findAllByPatientAndMeasurementType(patient, type, [sort: "time", order: "desc"])
                def cgmMeasurements = ContinuousBloodSugarEvent.findAllByMeasurementInListAndTimeBetween(measurements, timeFilter.start, timeFilter.end, [sort: "time", order: "desc"])

                cgmMeasurements*.measurement.unique() //The list of measurement holding ContinuousBloodSugarMeasurement that fall within the timefilter period
            } else {
                def measurements = Measurement.findAllByPatientAndMeasurementType(patient, type, [sort: "time", order: "desc"])
                measurements.findAll { !it.continuousBloodSugarEvents.isEmpty() }
            }
        } else {
            if(timeFilter.isLimited) {
                Measurement.findAllByPatientAndMeasurementTypeAndTimeBetween(patient, type, timeFilter.start, timeFilter.end, [sort: "time"])
            } else {
                Measurement.findAllByPatientAndMeasurementType(patient, type, [sort: "time"])
            }
        }
    }



    private List<List<Float>> findThresholdValues(type, patient) {
        Threshold threshold = patient.getThreshold(type)
        if (!threshold) {
            return [[], []]
        }
        switch (type) {
            case MeasurementTypeName.PULSE:
            case MeasurementTypeName.WEIGHT:
            case MeasurementTypeName.HEMOGLOBIN:
            case MeasurementTypeName.SATURATION:
            case MeasurementTypeName.CRP:
            case MeasurementTypeName.BLOODSUGAR:
                return numericThresholdValues(threshold as NumericThreshold)
        }
        [[], []]
    }

    private def bloodPressureSeries(List<Measurement> measurements) {
        def systolic = measurements.collect { measurement ->
            [measurement.time.time, measurement.systolic, hourAndMinute(measurement.time), note(measurement), i18nService.message(code: "patient.overview.measurements.SYSTOLIC_BLOOD_PRESSURE") + ": ${measurement.systolic as int}"]
        }
        def diastolic = measurements.collect { measurement ->
            [measurement.time.time, measurement.diastolic, hourAndMinute(measurement.time), note(measurement), i18nService.message(code: "patient.overview.measurements.DIASTOLIC_BLOOD_PRESSURE") + ": ${measurement.diastolic as int}"]
        }

        [systolic, diastolic]
    }

    private def systolicGraphSeries(List<Measurement> measurements) {
        def series = measurements.collect { measurement ->
            [measurement.time.time, measurement.systolic, hourAndMinute(measurement.time), note(measurement), i18nService.message(code: "patient.overview.measurements.SYSTOLIC_BLOOD_PRESSURE") + ": ${measurement.systolic as int}"]
        }

        [series]
    }

    private def diastolicGraphSeries(List<Measurement> measurements) {

        def series = measurements.collect { measurement ->
            [measurement.time.time, measurement.diastolic, hourAndMinute(measurement.time), note(measurement), i18nService.message(code: "patient.overview.measurements.DIASTOLIC_BLOOD_PRESSURE") + ": ${measurement.diastolic as int}"]
        }

        [series]
    }

    private def pulseGraphSeries(List<Measurement> measurements) {
        def series = measurements.collect { measurement ->
            [measurement.time.time, measurement.value, hourAndMinute(measurement.time), note(measurement),  i18nService.message(code: "patient.overview.table.label.PULSE") + ": ${measurement}"]
        }

        [series]
    }

    private def generalGraphSeries(List<Measurement> measurements) {
        def series = measurements.collect { measurement ->
            [measurement.time.time, measurement.value, hourAndMinute(measurement.time), note(measurement), "${measurement}"]
        }

        [series]
    }

    private def continuousBloodGlucoseGraphSeries(List<Measurement> measurements, TimeFilter timeFilter) {
        def continuousBloodSugarMeasurementSeries = []
        def coulometerReadingEventSeries = []
        def insulinEventSeries = []
        def exerciseEventSeries = []
        def stateOfHealthEventSeries = []
        def mealEventSeries = []
        def genericEventSeries = []


        measurements.each { measurement ->
            measurement.continuousBloodSugarEvents.each { cgmEvent ->
                if(timeFilter.isLimited && (cgmEvent.time.before(timeFilter.start)|| cgmEvent.time.after(timeFilter.end))) {
                    return  // The datapoint falls outside the time filter period
                }

                if(cgmEvent.class.name == "org.opentele.server.model.cgm.ContinuousBloodSugarMeasurement") {
                    continuousBloodSugarMeasurementSeries << [cgmEvent.time.time, cgmEvent.glucoseValueInmmolPerl, hourAndMinute(cgmEvent.time), note(measurement), "${cgmEvent.glucoseValueInmmolPerl}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.CoulometerReadingEvent") {''
                    coulometerReadingEventSeries << [cgmEvent.time.time, cgmEvent.glucoseValueInmmolPerl, hourAndMinute(cgmEvent.time), note(measurement), "${cgmEvent.glucoseValueInmmolPerl}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.InsulinEvent") {
                    insulinEventSeries << [cgmEvent.time.time, 15, hourAndMinute(cgmEvent.time), note(measurement), "${i18nService.message(code: 'graph.cgm.insulinEventSeries', args: [cgmEvent.insulinType, cgmEvent.units])}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.ExerciseEvent") {
                    exerciseEventSeries << [cgmEvent.time.time, 16, hourAndMinute(cgmEvent.time), note(measurement), "${i18nService.message(code: 'graph.cgm.exerciseEventSeries', args: [cgmEvent.exerciseType, cgmEvent.durationInMinutes, cgmEvent.exerciseIntensity])}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.MealEvent") {
                    mealEventSeries << [cgmEvent.time.time, 17, hourAndMinute(cgmEvent.time), note(measurement), "${i18nService.message(code: 'graph.cgm.exerciseMealSeries', args: [cgmEvent.carboGrams, cgmEvent.foodType])}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.StateOfHealthEvent") {
                    stateOfHealthEventSeries  << [cgmEvent.time.time, 18, hourAndMinute(cgmEvent.time), note(measurement), "${cgmEvent.stateOfHealth}"]
                } else if(cgmEvent.class.name == "org.opentele.server.model.cgm.GenericEvent") {
                    genericEventSeries << [cgmEvent.time.time, 19, hourAndMinute(cgmEvent.time), note(measurement), "${i18nService.message(code: 'graph.cgm.genericEventSeries', args: [cgmEvent.indicatedEvent])}"]
                } else {
                    log.debug("Unsupported ContinuousBloodSugarEvent type: ${cgmEvent}")
                }
            };
        }
        [continuousBloodSugarMeasurementSeries, coulometerReadingEventSeries, insulinEventSeries, exerciseEventSeries, stateOfHealthEventSeries , mealEventSeries, genericEventSeries]
    }

    private def hourAndMinute(time) {
        "'${hourAndMinuteDateFormatter.get().format(time)}'"
    }

    private def note(Measurement measurement) {
        def note = measurement.measurementNodeResult?.completedQuestionnaire?.acknowledgedNote ?: ''
        "${StringUtils.abbreviate(note, 80)}"
    }

    private List<Double> calculateMinAndMaxY(series, alertValues, warningValues) {
        def allMeasurements = series.inject([]) { accumulator, value -> accumulator.plus(value)}
        def valuesFromSeries = allMeasurements.collect { it[1] }
        def allValues = valuesFromSeries.plus(alertValues).plus(warningValues)
        [allValues.min(), allValues.max()] as List<Double>
    }

    private def calculateStartAndEnd(List<Measurement> measurements, TimeFilter timeFilter) {
        def timestamps = measurements.collect { it.time }
        calculateStartAndEndFromDates(timestamps, timeFilter)
    }

    private def calculateStartAndEndFromDates(List<Date> timestamps, TimeFilter timeFilter) {
        if (timeFilter.isLimited) {
            [timeFilter.start.getTime(), timeFilter.end.getTime()]
        } else {
            Calendar startTime = Calendar.getInstance()
            startTime.setTime(timestamps.min())
            setToMidnightBefore(startTime)

            Calendar endTime = Calendar.getInstance()
            endTime.setTime(timestamps.max())
            setToMidnightBefore(endTime)
            endTime.add(Calendar.DATE, 1)

            [startTime.getTime().getTime(), endTime.getTime().getTime()]
        }
    }

    private void setToMidnightBefore(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }


    private List<List<Float>> numericThresholdValues(NumericThreshold threshold) {
        [[threshold.alertHigh, threshold.alertLow], [threshold.warningHigh, threshold.warningLow]]
    }

    private List<List<Float>> systolicThresholdValues(BloodPressureThreshold threshold) {
        threshold ? [[threshold.systolicAlertHigh, threshold.systolicAlertLow], [threshold.systolicWarningHigh, threshold.systolicWarningLow]] : [[], []]
    }

    private List<List<Float>> diastolicThresholdValues(BloodPressureThreshold threshold) {
        threshold ? [[threshold.diastolicAlertHigh, threshold.diastolicAlertLow], [threshold.diastolicWarningHigh, threshold.diastolicWarningLow]] : [[], []]
    }
}
