package org.opentele.server.provider.identification

import spock.lang.Specification

class CprIdentificationValidatorSpec extends Specification {
    def validator

    def setup() {
        validator = new CprIdentificationValidator()
    }

    def "cpr number with length different from 10 fails validation"() {
        given:
        def errors = new TestErrors()

        when:
        validator.validateIdentification("123456789", validator, errors)

        then:
        errors.errorMessage == "validate.patient.cpr.length"
        errors.field == 'cpr'
    }

    def "null cpr number fails validation"() {
        given:
        def errors = new TestErrors()

        when:
        validator.validateIdentification(null, validator, errors)

        then:
        errors.errorMessage == "validate.patient.cpr.blank"
        errors.field == 'cpr'
    }

    def "valid cpr number passes validation"() {
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
