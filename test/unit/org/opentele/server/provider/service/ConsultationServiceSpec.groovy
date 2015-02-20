package org.opentele.server.provider.service

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.provider.ConsultationService
import org.opentele.server.model.Clinician
import org.opentele.server.model.Consultation
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.MeasurementTypeName
import spock.lang.Specification

@TestFor(ConsultationService)
@Build([Patient, Clinician, Consultation, Measurement, MeasurementType])
class ConsultationServiceSpec extends Specification {

    Patient patient
    Clinician clinician

    def setup() {
        patient = Patient.build(firstName: 'Nancy Ann', lastName: 'Berggreen', cpr: '1234567890')
        clinician = Clinician.build(firstName: 'Helle', lastName: 'Andersen')

        MeasurementType.build(name: MeasurementTypeName.WEIGHT).save(failOnError: true)
        MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE).save(failOnError: true)
        MeasurementType.build(name: MeasurementTypeName.SATURATION).save(failOnError: true)
        MeasurementType.build(name: MeasurementTypeName.PULSE).save(failOnError: true)
        MeasurementType.build(name: MeasurementTypeName.LUNG_FUNCTION).save(failOnError: true)
    }

    def "when I add a null params it works"() {
        when:
        Consultation consultation = service.addConsultation(null, null, null)

        then:
        !consultation.hasErrors()
    }

    def "when I add a consultation with weight it works"() {
        setup:
        def params = [showWEIGHT: 'true', weight: '123,1']

        when:
        Consultation consultation = service.addConsultation(patient, clinician, params)

        then:
        !consultation.hasErrors()
        consultation.measurements.size() == 1
    }

    def "when I add a consultation with blood pressure values it works"() {
        setup:
        def params = [showBLOOD_PRESSURE: 'true', systolic: '100', diastolic: '100', pulse: '100']

        when:
        Consultation consultation = service.addConsultation(patient, clinician, params)

        then:
        !consultation.hasErrors()
        consultation.measurements.size() == 2
    }

    def "when I add a consultation with saturation values it works"() {
        setup:
        def params = [showSATURATION: 'true', saturation: '100', saturationPuls: '100']

        when:
        Consultation consultation = service.addConsultation(patient, clinician, params)

        then:
        !consultation.hasErrors()
        consultation.measurements.size() == 2
    }

    def "when I add a consultation with lung function values it works"() {
        setup:
        def params = [showLUNG_FUNCTION: 'true', lungfunction: '100', saturationPuls: '100']

        when:
        Consultation consultation = service.addConsultation(patient, clinician, params)

        then:
        !consultation.hasErrors()
        consultation.measurements.size() == 1
    }
}
