package org.opentele.server.provider.identification

class CprIdentificationValidator {

    def validateIdentification(val, obj, errors) {
        if (!val) {
            errors.rejectValue("cpr", "validate.patient.cpr.blank")
        } else if (val?.length() != 10) {
            errors.rejectValue("cpr", "validate.patient.cpr.length")
        }
    }

    String toString() {
        def owningClass = this.metaClass.owner.class
        owningClass.getMethod('toString').invoke(this.metaClass.owner) // Needed else calling .toString() on e.g. patient instance returns the type name of the validator
    }
}
