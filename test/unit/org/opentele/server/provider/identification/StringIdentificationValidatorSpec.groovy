package org.opentele.server.provider.identification

import spock.lang.Specification

class StringIdentificationValidatorSpec extends Specification {
    def validator

    def setup() {
        validator = new StringIdentificationValidator()
    }

    def "null identification number fails validation"() {
        given:
        def errors = new TestErrors()

        when:
        validator.validateIdentification(null, validator, errors)

        then:
        errors.errorMessage == "validate.patient.identification.blank"
        errors.field == 'cpr'
    }

    def "valid identification number passes validation"() {
        given:
        def errors = new TestErrors()

        when:
        validator.validateIdentification("1234567890", validator, errors)

        then:
        errors.errorMessage == null
        errors.field == null
    }

    class TestErrors {
        def field
        def errorMessage

        def rejectValue(field, errorMessage) {
            this.field = field
            this.errorMessage = errorMessage
        }
    }
}
