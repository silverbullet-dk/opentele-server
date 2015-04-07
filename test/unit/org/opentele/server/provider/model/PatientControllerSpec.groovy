package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.server.core.model.types.Sex
import org.opentele.server.provider.PatientIdentificationService
import org.opentele.server.provider.PatientService
import org.opentele.server.model.Patient
import spock.lang.Specification


@TestFor(PatientController)
@Build([Patient])
class PatientControllerSpec extends Specification {
    def setup() {
        controller.patientService = Mock(PatientService)
        controller.patientIdentificationService = Mock(PatientIdentificationService)
    }

    def "test that sendPassword calls patientService if the patient exist"(){
        setup:
        def patient = Patient.build()

        when:
        params.id = patient.id

        and:
        controller.sendPassword()

        then:
        1 * controller.patientService.sendPassword(patient)
        flash.message == 'patient.send-password.done'
        response.redirectedUrl == "/patient/show/${patient.id}"
    }

    def "test that sendPassword does not call patientService if the patient does not exist"(){
        when:
        controller.sendPassword()

        then:
        0 * controller.patientService.sendPassword(_)
        flash.message == 'default.not.found.message'
        response.redirectedUrl == "/patient/list"
    }

    def "can get json representation with a calculation of the sex of a patient based on identification"() {
        setup:
        params.identification = '251126-6699'
        controller.patientIdentificationService.calculateSex(_) >> Sex.MALE

        when:
        controller.patientSex()

        then:
        response.status == 200
        def body = new JSONObject(response.text)
        body.sex == 'MALE'
    }

    def "calculation of the sex of a patient returns unknown if no identification is supplied"() {
        when:
        controller.patientSex()

        then:
        response.status == 200
        def body = new JSONObject(response.text)
        body.sex == 'UNKNOWN'
    }
}
