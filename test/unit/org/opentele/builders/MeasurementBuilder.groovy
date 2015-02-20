package org.opentele.builders
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.core.model.types.MeasurementTypeName

class MeasurementBuilder {
    private MeasurementTypeName measurementTypeName
    private CompletedQuestionnaire completedQuestionnaire
    private Date time = new Date()
    private int systolic = 120
    private int diastolic = 65
    private double value = 65
    private isBeforeMeal = false
    private isAfterMeal = false
    private boolean ignored = false
    private boolean exported = false

    MeasurementBuilder ofType(MeasurementTypeName measurementTypeName) {
        this.measurementTypeName = measurementTypeName
        this
    }

    MeasurementBuilder atTime(int year, int month, int day, int hour=0, int minute=0) {
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        time = calendar.getTime()
        this
    }

    MeasurementBuilder inQuestionnaire(CompletedQuestionnaire completedQuestionnaire) {
        this.completedQuestionnaire = completedQuestionnaire
        this
    }

    MeasurementBuilder alreadyExported() {
        this.exported = true
        this
    }

    MeasurementBuilder ignored(ignored) {
        this.ignored = ignored
        this
    }

    MeasurementBuilder withSystolic(double systolic) {
        this.systolic = systolic
        this
    }

    MeasurementBuilder beforeMeal()
    {
        this.isBeforeMeal = true
        this.isAfterMeal = false
        this
    }

    MeasurementBuilder afterMeal()
    {
        this.isBeforeMeal = false
        this.isAfterMeal = true
        this
    }

    MeasurementBuilder withValue(double value) {
        this.value = value
        this
    }

    Measurement build() {
        def patient = completedQuestionnaire.patient
        def measurementType = MeasurementType.findByName(measurementTypeName)
        if (measurementType == null) {
            throw new RuntimeException("No measurement types with name '${measurementTypeName}'")
        }

        switch(measurementTypeName) {
            case MeasurementTypeName.BLOOD_PRESSURE:
                return buildBloodPressureMeasurement(patient, measurementType)
            case MeasurementTypeName.CTG:
                return buildCtgMeasurement(patient, measurementType)
            case MeasurementTypeName.PULSE:
            case MeasurementTypeName.LUNG_FUNCTION:
                return buildSimpleMeasurement(patient, measurementType)
            case MeasurementTypeName.BLOODSUGAR:
                return buildBloodsugarMeasurement(patient, measurementType)
            case MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT:
                return buildContinuousBloodSugarMeasurement(patient, measurementType)
            default:
                throw new RuntimeException('No measurement type name specified')
        }
    }


    private Measurement buildSimpleMeasurement(patient, measurementType) {
        def result = Measurement.build(patient: patient, measurementType: measurementType, value: value, time: time)
        result.save(failOnError: true)
        result
    }

    private Measurement buildCtgMeasurement(patient, measurementType) {
        def measurementNodeResult = MeasurementNodeResult.build(nodeIgnored:false, measurementType: measurementType, completedQuestionnaire: completedQuestionnaire)
        measurementNodeResult.save(failOnError: true)

        def result = Measurement.build(patient: patient, measurementType: measurementType, measurementNodeResult: measurementNodeResult, fhr: '', mhr: '', qfhr: '', toco: '', time: time, startTime: new Date(), endTime: new Date(), exported: exported)
        result.save(failOnError: true)
        result
    }

    private Measurement buildBloodPressureMeasurement(patient, measurementType) {
        def result
        if (ignored) {
            def measurementNodeResult = MeasurementNodeResult.build(nodeIgnored:false, measurementType: measurementType, completedQuestionnaire: completedQuestionnaire)
            measurementNodeResult.save(failOnError: true)
            measurementNodeResult.nodeIgnored = true

            result = Measurement.build(patient: patient, measurementType: measurementType, systolic: systolic, diastolic: diastolic, measurementNodeResult: measurementNodeResult, time: time)
        } else {
            result = Measurement.build(patient: patient, measurementType: measurementType, systolic: systolic, diastolic: diastolic, time: time)
        }

        result.save(failOnError: true)
        result
    }

    private Measurement buildBloodsugarMeasurement(patient, measurementType) {
        def result = Measurement.build(patient: patient, measurementType: measurementType, value: value, time: time, isBeforeMeal: isBeforeMeal, isAfterMeal: isAfterMeal)
        result.save(failOnError: true)
        result
    }

    private Measurement buildContinuousBloodSugarMeasurement(patient, measurementType) {
        def result = Measurement.build(patient: patient, measurementType: measurementType, time: time, continuousBloodSugarEvents:[])
        result.save(failOnError: true)
        result
    }
}
