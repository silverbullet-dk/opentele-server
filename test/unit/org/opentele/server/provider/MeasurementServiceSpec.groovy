package org.opentele.server.provider

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.MeasurementBuilder
import org.opentele.builders.MeasurementTypeBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.server.core.I18nService
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.util.TimeFilter
import org.opentele.server.model.*
import org.opentele.server.model.cgm.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.cgm.*
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(MeasurementService)
@Build([Patient, Measurement, MeasurementNodeResult, QuestionnaireHeader, CompletedQuestionnaire, MeasurementType, PatientQuestionnaire, Clinician, PatientBooleanNode, BooleanNode, ContinuousBloodSugarEvent, ContinuousBloodSugarMeasurement, CoulometerReadingEvent, HyperAlarmEvent, HypoAlarmEvent, ImpendingHyperAlarmEvent, ImpendingHypoAlarmEvent, MealEvent, StateOfHealthEvent, ExerciseEvent, InsulinEvent, GenericEvent])
@TestMixin(GrailsUnitTestMixin)
class MeasurementServiceSpec extends Specification {
    Patient patient
    CompletedQuestionnaire completedQuestionnaire
    MeasurementType bloodPressureMeasurementType
    MeasurementType ctgMeasurementType
    MeasurementType pulseMeasurementType
    MeasurementType lungFunctionMeasurementType
    MeasurementType bloodsugarMeasurementType
    MeasurementType continuousBloodSugarMeasurementType

    def setup() {
        // Ugly hack to avoid a null pointer exception, since the springSecurityService can not be injected
        service.i18nService = Mock(I18nService)

        // The encodeAsJavaScript() method is not added to String in unit tests, and it is used within MeasurementService.
        // In fact, the use of encodeAsJavaScript SHOULD probably not happen in the service, but related controllers...
        String.metaClass.encodeAsJavaScript { it }

        patient = new PatientBuilder().build()
        completedQuestionnaire = new CompletedQuestionnaireBuilder().forPatient(patient).build()

        bloodPressureMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).build()
        ctgMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.CTG).build()
        pulseMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.PULSE).build()
        lungFunctionMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.LUNG_FUNCTION).build()
        bloodsugarMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.BLOODSUGAR).build()
        continuousBloodSugarMeasurementType = new MeasurementTypeBuilder().ofType(MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT).build()

        patient.save()
        patient.thresholds.add(new BloodPressureThreshold(type: bloodPressureMeasurementType, systolicAlertLow: 70, systolicAlertHigh: 190, systolicWarningHigh: 180, systolicWarningLow: 80, diastolicAlertHigh: 100, diastolicWarningHigh: 90, diastolicWarningLow: 50, diastolicAlertLow: 40))
    }

    def "only gives table data for measurement types with data"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build()

        when:
        def tableData = service.dataForTables(patient, TimeFilter.all())

        then:
        tableData.size() == 1
        tableData[0].type == MeasurementTypeName.BLOOD_PRESSURE
    }

    void "gives all data for tables including ignored measurements"() {
        setup:
        9.times { new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build() }
        new MeasurementBuilder().ignored(true).ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.CTG).inQuestionnaire(completedQuestionnaire).build()

        when:
        def tableData = service.dataForTables(patient, TimeFilter.all())
        and:
        def bloodPressureMeasurements = tableData[0]
        def ctgMeasurements = tableData[1]

        then:
        bloodPressureMeasurements.type == MeasurementTypeName.BLOOD_PRESSURE
        bloodPressureMeasurements.measurements.size == 10

        ctgMeasurements.type == MeasurementTypeName.CTG
        ctgMeasurements.measurements.size == 1
    }

    void "marks systolic table data higher than alert threshold"() {
        given:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).withSystolic(200).inQuestionnaire(completedQuestionnaire).build()

        expect:
        !singleMeasurement().warning()
        singleMeasurement().alert()
    }

    void "marks systolic table data higher than warning threshold"() {
        given:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).withSystolic(185).inQuestionnaire(completedQuestionnaire).build()

        expect:
        !singleMeasurement().alert()
        singleMeasurement().warning()
    }

    void "does not mark systolic table data within thresholds"() {
        given:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).withSystolic(120).inQuestionnaire(completedQuestionnaire).build()

        expect:
        !singleMeasurement().alert()
        !singleMeasurement().warning()
    }

    void "marks systolic table data lower than warning threshold"() {
        given:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).withSystolic(75).inQuestionnaire(completedQuestionnaire).build()

        expect:
        !singleMeasurement().alert()
        singleMeasurement().warning()
    }

    void "marks systolic table data lower than alert threshold"() {
        given:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).withSystolic(65).inQuestionnaire(completedQuestionnaire).build()

        expect:
        singleMeasurement().alert()
        !singleMeasurement().warning()
    }

    void "gives three blood pressure graphs"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.all())

        then:
        graphData.size() == 3
        graphData[0].type == 'BLOOD_PRESSURE'
        graphData[1].type == 'SYSTOLIC_BLOOD_PRESSURE'
        graphData[2].type == 'DIASTOLIC_BLOOD_PRESSURE'
    }

    void "sets start and end times to time filter interval"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.FEBRUARY, 3, 14, 30).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.fromParams([filter: 'CUSTOM', start: date(2013, Calendar.FEBRUARY, 1), end: date(2013, Calendar.FEBRUARY, 4)]))

        then:
        graphData[0].start == date(2013, Calendar.FEBRUARY, 1).getTime()
        graphData[0].end == date(2013, Calendar.FEBRUARY, 5).getTime()
    }

    void "sets start and end times to start of all measurements and end of all measurements when no time filter is applied"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.FEBRUARY, 1, 14, 25).inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.FEBRUARY, 3, 14, 30).inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.FEBRUARY, 7, 14, 35).inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.PULSE).atTime(2013, Calendar.FEBRUARY, 8, 14, 40).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.all())

        then:
        graphData.size() == 4 // Three blood pressure graphs,  one pulse graph
        graphData[0].start == date(2013, Calendar.FEBRUARY, 1).getTime()
        graphData[0].end == date(2013, Calendar.FEBRUARY, 9).getTime()
        graphData[1].start == graphData[0].start
        graphData[1].end == graphData[0].end
        graphData[2].start == graphData[0].start
        graphData[2].end == graphData[0].end
        graphData[3].start == graphData[0].start
        graphData[3].end == graphData[0].end
    }

    void "displays date label for every day when showing at most 32 days"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.JANUARY, 10, 14, 25).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.fromParams([filter: 'CUSTOM', start: date(2013, Calendar.JANUARY, 1), end: date(2013, Calendar.FEBRUARY, 1)]))

        then:
        graphData[0].ticksX.size() == 33
        graphData[0].ticksX[0] == '2013-01-01'
        graphData[0].ticksX[1] == '2013-01-02'

        graphData[0].ticksX[31] == '2013-02-01'
        graphData[0].ticksX[32] == '2013-02-02'
    }

    void "displays empty acknowledgement text when no acknowledgement note exists"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.JANUARY, 10, 14, 25).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.fromParams([filter: 'CUSTOM', start: date(2013, Calendar.JANUARY, 1), end: date(2013, Calendar.FEBRUARY, 1)]))

        then:
        graphData[0].series[0][0][3] == ''
    }

    @Unroll
    void "test displays at most 20 date labels when showing more than 32 days"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013, Calendar.JANUARY, 10, 14, 25).inQuestionnaire(completedQuestionnaire).build()

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.fromParams([filter: 'CUSTOM', start: date(2013, Calendar.JANUARY, 1), end: date(2013, Calendar.FEBRUARY, endDay)]))
        then:

        graphData[0].ticksX.size() == ticks
        graphData[0].ticksX[0] == first
        graphData[0].ticksX[1] == second
        graphData[0].ticksX[2] == third


        graphData[0].ticksX[-2] == secondLast
        graphData[0].ticksX[-1] == last

        where:
        endDay | ticks | first        | second       | third        | secondLast   | last
        2      | 18    | '2013-01-01' | '2013-01-02' | '2013-01-04' | '2013-02-01' | '2013-02-03' // 33 days - label every 2nd day
        9      | 21    | '2013-01-01' | '2013-01-03' | '2013-01-05' | '2013-02-08' | '2013-02-10' // 40 days - label every 2nd day
        10     | 15    | '2013-01-01' | '2013-01-03' | '2013-01-06' | '2013-02-08' | '2013-02-11' // 41 days - label every 3rd day
    }

    void "test ignores invalid measurements for graph data"() {
        setup:
        9.times { new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build() }
        new MeasurementBuilder().ignored(true).ofType(MeasurementTypeName.BLOOD_PRESSURE).inQuestionnaire(completedQuestionnaire).build()

        10.times { new MeasurementBuilder().ofType(MeasurementTypeName.CTG).inQuestionnaire(completedQuestionnaire).build() }

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.all())
        and:
        def bloodPressureMeasurements = graphData[0]

        then:
        bloodPressureMeasurements.series[0].size() == 9 // Systolic
        bloodPressureMeasurements.series[1].size() == 9 // Diastolic
    }


    void "test gives sensible maximum and minimum values for graph axis"() {
        setup:
        [32, 35, 62].each {
            new MeasurementBuilder().ofType(MeasurementTypeName.PULSE).withValue(it).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def graphData = service.dataForGraphs(patient, TimeFilter.all())

        then:
        // Minimum, maximum, and ticks are calculated by YAxisTickCalculator, which has its own spec and all, so
        // we won't test this in detail
        graphData[0].minY == 30
        graphData[0].maxY == 130
        graphData[0].ticksY[0].value == 30
        graphData[0].ticksY[1].value == 40

        graphData[0].ticksY[-1].value == 130
    }

    void "test bloodsugar data is empty if no bloodsugar measurement is present"() {
        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        then:
        bloodsugar == []
    }

    void "test bloodsugar data correct values are returned"() {
        setup:
        [34: 1.2, 35: 2.2, 36: 3.2].each { int second, value ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, 10, 12, second).withValue(value).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        and:
        def data = bloodsugar[0]

        then:
        bloodsugar.size() == 1

        data.date.format("dd/MM/yyyy") == "10/01/2013"
        !data.zeroToFour
        !data.fiveToTen
        data.elevenToSixteen.size() == 3
        !data.seventeenToTwentyThree

        data.elevenToSixteen*.measurement*.value == [1.2, 2.2, 3.2]
    }

    void "test bloodsugar on different dates"() {
        setup:
        [12, 14].each { hour ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, hour, 12, 35).
                    withValue(2.2).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        then:
        bloodsugar.size() == 2
        //noinspection GroovyAssignabilityCheck
        bloodsugar*.date*.format('dd/MM/yyyy') == ["12/01/2013", "14/01/2013"]
    }

    void "test bloodsugar one measurement per hour"() {
        setup:
        (0..23).each { hour ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, 12, hour, 35).
                    withValue(2.2).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        and:
        BloodsugarDataPerDate data = bloodsugar[0]

        then:
        bloodsugar.size() == 1

        // Check that the correct number of measurements go into each time period.
        data.zeroToFour.size() == 5
        data.fiveToTen.size() == 6
        data.elevenToSixteen.size() == 6
        data.seventeenToTwentyThree.size() == 7
    }

    void "test bloodsugar ordering of dates"() {
        setup:
        // Create data for different days, in an unsorted order.
        [12, 14, 10, 11, 13].each { hour ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, hour, 12, 35).
                    withValue(2.2).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        then:
        bloodsugar.size() == 5

        // Check that dates are returned in sorted order.
        //noinspection GroovyAssignabilityCheck
        bloodsugar*.date*.format('dd/MM/yyyy') == ["10/01/2013", "11/01/2013", "12/01/2013", "13/01/2013", "14/01/2013"]
    }

    void "test bloodsugar ordering of measurements within period"() {
        setup:
        // Create data for different times in the same period, in an unsorted order.
        def secondToValue = [37: 5.2, 34: 2.2, 36: 4.2, 35: 3.2, 33: 1.2]
        secondToValue.each { int second, value ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, 10, 12, second).withValue(value).inQuestionnaire(completedQuestionnaire).build()
        }

        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())

        and:
        List<BloodsugarDataMeasurement> periodData = bloodsugar[0].elevenToSixteen

        // Check that measurements are returned in sorted order.

        then:
        periodData*.timestamp*.get(Calendar.MINUTE) == secondToValue.keySet().sort()
        periodData*.measurement*.value == secondToValue.sort { it.key }.values().toList()
    }

    void "test bloodsugar before or after meal"() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013,Calendar.JANUARY,10,12,34).withValue(1.2).inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013,Calendar.JANUARY,10,12,35).withValue(2.2).beforeMeal().inQuestionnaire(completedQuestionnaire).build()
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013,Calendar.JANUARY,10,12,36).withValue(3.2).afterMeal().inQuestionnaire(completedQuestionnaire).build()


        when:
        def bloodsugar = service.dataForBloodsugar(patient, TimeFilter.all())
        and:
        List<BloodsugarDataMeasurement> data = bloodsugar[0].elevenToSixteen

        then:
        !data[0].beforeMeal
        !data[0].afterMeal

        data[1].beforeMeal
        !data[1].afterMeal

        !data[2].beforeMeal
        data[2].afterMeal
    }

    void 'test blood sugar time filtering'() {
        setup:
        // Create data for different dates.
        [10: 1.2, 12: 2.2, 14: 3.2, 16: 4.2, 18: 5.2].each { int day, value ->
            new MeasurementBuilder().ofType(MeasurementTypeName.BLOODSUGAR).atTime(2013, Calendar.JANUARY, day, 12, 33).withValue(value).inQuestionnaire(completedQuestionnaire).build()
        }

        Date startDate = new Date(2013 - 1900, Calendar.JANUARY, 11, 0, 0, 0)
        Date endDate = new Date(2013 - 1900, Calendar.JANUARY, 17, 0, 0, 0)

        when:
        def timeFilter = TimeFilter.fromParams([filter: 'CUSTOM', start: startDate, end: endDate])
        and:
        def bloodSugar = service.dataForBloodsugar(patient, timeFilter)

        then:
        // Check that measurements are returned in sorted order.
        bloodSugar.size() == 3
        //noinspection GroovyAssignabilityCheck
        bloodSugar*.date*.format('dd/MM/yyyy') == ["12/01/2013", "14/01/2013", "16/01/2013"]
    }

    void 'test blood pressure with thresholds on patient gives systolic and diastolic blood pressure graph data'() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013,Calendar.JANUARY,10,12,34).withValue(1.2).inQuestionnaire(completedQuestionnaire).build()

        when:
        def bloodPressure = service.dataForGraphs(patient, TimeFilter.all())

        then:
        bloodPressure.size() == 3
        bloodPressure*.type == ['BLOOD_PRESSURE', 'SYSTOLIC_BLOOD_PRESSURE', 'DIASTOLIC_BLOOD_PRESSURE']
    }

    void 'test blood pressure without thresholds for patient does not give systolic and diastolic blood pressure graph data'() {
        setup:
        new MeasurementBuilder().ofType(MeasurementTypeName.BLOOD_PRESSURE).atTime(2013,Calendar.JANUARY,10,12,34).withValue(1.2).inQuestionnaire(completedQuestionnaire).build()
        patient.thresholds.clear()
        patient.save()

        when:
        def bloodPressure = service.dataForGraphs(patient, TimeFilter.all())

        then:
        bloodPressure.size() == 1
        bloodPressure*.type == ['BLOOD_PRESSURE']
    }

    void 'can create bloodsugardata even when isAfterMeal is null' () {
        setup:
        Measurement measurement = new Measurement()
        measurement.patient = patient
        measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.BLOODSUGAR)
        measurement.time = new Date()
        measurement.value = 4.0
        measurement.isBeforeMeal = true

        List<Measurement> list = new ArrayList<Measurement>()
        list.add(measurement)

        when:
        def res = service.createBloodsugarData(list)

        then:
        res != null
    }

    void 'can give all CGM measurements as graph data'() {
        setup:
        def measurement = new MeasurementBuilder().ofType(MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT).atTime(2013, Calendar.JANUARY, 10).inQuestionnaire(completedQuestionnaire).build()
        measurement.save(flush:true)
        long now = date(2013, Calendar.FEBRUARY, 2).time;
        (0..99).each { time ->
            //measurement.addToContinuousBloodSugarEvents(new ContinuousBloodSugarMeasurement(recordNumber: time, time: new Date(now + time*1000), glucoseValueInmmolPerl: time))
            ContinuousBloodSugarEvent event = new ContinuousBloodSugarMeasurement(recordNumber: time, time: new Date(now + time*1000), glucoseValueInmmolPerl: time)
            measurement.addToContinuousBloodSugarEvents(event)
        }

        measurement.addToContinuousBloodSugarEvents(new HyperAlarmEvent(recordNumber: 2000, time: new Date(now), glucoseValueInmmolPerl: 1))
        measurement.addToContinuousBloodSugarEvents(new HypoAlarmEvent(recordNumber: 2001, time: new Date(now), glucoseValueInmmolPerl: 1))
        measurement.addToContinuousBloodSugarEvents(new ImpendingHyperAlarmEvent(impendingNess: 10, recordNumber: 2002, time: new Date(now), glucoseValueInmmolPerl: 1))
        measurement.addToContinuousBloodSugarEvents(new ImpendingHypoAlarmEvent(impendingNess: 10, recordNumber: 2003, time: new Date(now), glucoseValueInmmolPerl: 1))
        CoulometerReadingEvent reading = new CoulometerReadingEvent(recordNumber: 2004, time: new Date(now), glucoseValueInmmolPerl: 1)
        measurement.addToContinuousBloodSugarEvents(reading)
        measurement.addToContinuousBloodSugarEvents(new InsulinEvent(recordNumber: 2005, time: new Date(now), insulinType: InsulinType.INTERMEDIATE, units: 10))
        measurement.addToContinuousBloodSugarEvents(new ExerciseEvent(recordNumber: 2006, time: new Date(now), exerciseIntensity: ExerciseIntensity.HIGH, exerciseType: ExerciseType.AEROBICS))
        measurement.addToContinuousBloodSugarEvents(new MealEvent(recordNumber: 2007, time: new Date(now), foodType: FoodType.BREAKFAST, carboGrams: 1))
        measurement.addToContinuousBloodSugarEvents(new StateOfHealthEvent(recordNumber: 2008, time: new Date(now), stateOfHealth: HealthState.DIZZY))
        measurement.addToContinuousBloodSugarEvents(new GenericEvent(recordNumber: 2009, time: new Date(now), indicatedEvent: "1"))

        measurement.save(failOnError: true)

        when:
        GraphData cgmData = service.dataForGraphs(patient, TimeFilter.all()).first()

        then:
        cgmData.series.size() == 7

        cgmData.series[0].size() == 100
        cgmData.series[0][0][1] == 0 // First value in series
        cgmData.series[0][99][1] == 99 // Last value in series

        cgmData.series[1].size() == 1
        cgmData.series[1][0][1] == reading.glucoseValueInmmolPerl

        cgmData.series[2].size() == 1
        cgmData.series[2][0][1] == 15 // insulin event

        cgmData.series[3].size() == 1
        cgmData.series[3][0][1] == 16 // exercise event

        cgmData.series[4].size() == 1
        cgmData.series[4][0][1] == 18 // state of health event

        cgmData.series[5].size() == 1
        cgmData.series[5][0][1] == 17 // meal event

        cgmData.series[6].size() == 1
        cgmData.series[6][0][1] == 19 // generic event

        cgmData.start == date(2013, Calendar.FEBRUARY, 2).time
        cgmData.end == date(2013, Calendar.FEBRUARY, 3).time

        cgmData.minY == 0
        cgmData.maxY == 100
        cgmData.type == 'CONTINUOUS_BLOOD_SUGAR_MEASUREMENT'

        cgmData.warningValues == [] // No warning values yet
        cgmData.alertValues == [] // No alert values yet
        cgmData.ids == null // Not used for anything anyway
    }

    private TableMeasurement singleMeasurement() {
        def tableData = service.dataForTables(patient, TimeFilter.all())
        def bloodPressureMeasurements = tableData[0]
        bloodPressureMeasurements.measurements[0] as TableMeasurement
    }

    private Date date(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.getTime()
    }
}
