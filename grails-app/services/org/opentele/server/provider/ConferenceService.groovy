package org.opentele.server.provider

import org.opentele.server.model.Conference
import org.opentele.server.model.ConferenceMeasurementDraft

class ConferenceService {

    def deleteConferenceMeasurementDraft(id) {
        ConferenceMeasurementDraft measurementDraft = ConferenceMeasurementDraft.findById(id)
        Conference conference = measurementDraft.conference

        conference.removeFromMeasurementDrafts(measurementDraft)
        measurementDraft.delete(flush: true)

        return conference
    }
}
