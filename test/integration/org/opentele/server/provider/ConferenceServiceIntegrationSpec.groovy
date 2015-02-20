package org.opentele.server.provider

import grails.test.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.Conference
import org.opentele.server.model.ConferenceLungFunctionMeasurementDraft
import org.opentele.server.model.Patient

class ConferenceServiceIntegrationSpec extends IntegrationSpec {
    ConferenceService conferenceService

    Conference conference

    def setup() {
        def patient = Patient.build().save(failOnError: true)
        def clinician = Clinician.build().save(failOnError: true)
        conference = Conference.build(patient: patient, clinician: clinician, measurements: [], measurementDrafts: []).save(failOnError: true)
    }

    void "can delete measurement draft"() {
        given:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: true)
        lungFunctionMeasurementDraft.save(failOnError: true)
        def oldConferenceVersion = conference.version

        when:
        def updated = conferenceService.deleteConferenceMeasurementDraft(lungFunctionMeasurementDraft.id)


        then:
        updated.measurementDrafts.empty
        updated.version == oldConferenceVersion + 1
    }
}
