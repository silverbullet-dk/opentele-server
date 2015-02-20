package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.provider.PatientService
import org.opentele.server.model.Patient
import spock.lang.Specification


@TestFor(PatientController)
@Build([Patient])
class PatientControllerSpec extends Specification {
    def setup() {
        controller.patientService = Mock(PatientService)
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
}
