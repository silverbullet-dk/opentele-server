package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.apache.commons.logging.Log
import org.opentele.server.provider.ConsultationService
import org.opentele.server.model.Clinician
import org.opentele.server.model.Consultation
import org.opentele.server.model.Patient
import spock.lang.Specification

@TestFor(ConsultationController)
@Build([Patient, Clinician, Consultation])
class ConsultationControllerSpec extends Specification {

    def consultationService = Mock(ConsultationService)
    Clinician clinician
    Patient patient
    Patient patientInContext

    void setup() {

        clinician = Clinician.build(videoUser: 'user', videoPassword: 'pass')
        controller.log = Mock(Log)

        patientInContext = Patient.build()
        session.patientId = patientInContext.id
    }
}
