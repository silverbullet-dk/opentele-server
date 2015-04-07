package org.opentele.server.provider.identification

import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.core.model.types.Sex
import org.opentele.server.model.Patient

class CprIdentification {

    def setupValidation() {
        CreatePatientCommand.mixin(CprIdentificationValidator)
        Patient.mixin(CprIdentificationValidator)
    }

    def calculateSex(cpr) {
        def cprShort = removeSeparator(cpr)
        if (!cpr || cprShort.length() != 10) {
            return Sex.UNKNOWN
        }
        return Long.parseLong(cprShort) % 2 == 0 ? Sex.FEMALE : Sex.MALE
    }

    def formatForDisplay(cpr) {
        if (cpr.length() != 10) {
            return cpr
        }
        return "${cpr[0..5]}-${cpr[6..9]}"
    }

    def formatForStorage(identification) {
        return removeSeparator(identification)
    }

    private def removeSeparator(cprRaw) {
        String cpr = cprRaw?.replaceAll(" ", "")
        cpr = cpr?.replaceAll("-", "")

        return cpr
    }
}
