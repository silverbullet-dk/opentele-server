package org.opentele.server.provider

import org.opentele.server.provider.identification.CprIdentification
import org.opentele.server.provider.identification.StringIdentification

class PatientIdentificationService {

    def grailsApplication

    def setupValidation() {

        identificationScheme().setupValidation()
    }

    def calculateSex(identification) {

        return identificationScheme().calculateSex(identification)
    }

    def formatForDisplay(identification) {

        if (identification == null) {
            return null
        }

        return identificationScheme().formatForDisplay(identification)
    }

    def formatForStorage(identification) {

        if (identification == null) {
            return null
        }

        return identificationScheme().formatForStorage(identification)
    }

    private def identificationScheme() {

        if (grailsApplication.config.patient.identification.isCpr) {
            return new CprIdentification()
        }

        return new StringIdentification()
    }
}