package org.opentele.server.provider.identification

import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.core.model.types.Sex
import org.opentele.server.model.Patient

class StringIdentification {

    def setupValidation() {
        CreatePatientCommand.mixin(StringIdentificationValidator)
        Patient.mixin(StringIdentificationValidator)
    }

    def calculateSex(identification) {
        return Sex.UNKNOWN
    }

    def formatForDisplay(identification) {
        return identification
    }

    def formatForStorage(identification) {
        return identification
    }
}
