package org.opentele.server.provider

import org.opentele.server.model.*
import org.opentele.server.core.model.types.GlucoseInUrineValue
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.ProteinValue
import org.opentele.server.core.model.types.Unit

class ConsultationService {

    def Consultation addConsultation(Patient patient, Clinician clinician, def params) {

        Date now = new Date()
        Consultation consultation = new Consultation(clinician: clinician, patient: patient, consultationDate: now)

        if (!params)
            return consultation

        Measurement measurement = new Measurement()
        if (params.showWEIGHT) {
            measurement.patient = patient
            measurement.time = now
            measurement.unit = Unit.KILO
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.WEIGHT)
            measurement.value = getDouble(params.weight)
            consultation.addToMeasurements(measurement)
        }

        if (params.showBLOOD_PRESSURE) {
            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE)
            measurement.unit = Unit.MMHG
            measurement.systolic = getDouble(params.systolic)
            measurement.diastolic = getDouble(params.diastolic)
            consultation.addToMeasurements(measurement)

            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            measurement.unit = Unit.BPM
            measurement.value = getDouble(params.pulse)
            consultation.addToMeasurements(measurement)
        }

        if (params.showSATURATION) {
            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.unit = Unit.PERCENTAGE
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.SATURATION)
            measurement.value = getDouble(params.saturation)
            consultation.addToMeasurements(measurement)

            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            measurement.unit = Unit.BPM
            measurement.value = getDouble(params.saturationPuls)
            consultation.addToMeasurements(measurement)
        }

        if (params.showLUNG_FUNCTION) {
            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.unit = Unit.LITER
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.LUNG_FUNCTION)
            measurement.value = getDouble(params.lungfunction)
            consultation.addToMeasurements(measurement)
        }

        if (params.showURINE_COMBI) {
            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.unit = Unit.GLUCOSE
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE_GLUCOSE)
            measurement.glucoseInUrine = GlucoseInUrineValue.fromString(params.urine_glucose)
            consultation.addToMeasurements(measurement)

            measurement = new Measurement()
            measurement.patient = patient
            measurement.time = now
            measurement.unit = Unit.PROTEIN
            measurement.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE)
            measurement.protein = ProteinValue.fromString(params.urine)
            consultation.addToMeasurements(measurement)
        }

        if (consultation.measurements)
            consultation.save(flush: true)

        return consultation
    }

    Double getDouble(String value) {
        try {
            value.replaceAll(',', '.').toDouble()
        } catch (Exception e) {
            null
        }
    }
}
