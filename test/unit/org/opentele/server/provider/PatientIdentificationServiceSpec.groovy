package org.opentele.server.provider


import grails.test.mixin.*
import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.core.model.types.Sex
import org.opentele.server.model.Patient
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PatientIdentificationService)
class PatientIdentificationServiceSpec extends Specification {

    @Unroll
    def "can calculate sex when patient identification is CPR number"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        def sex = service.calculateSex(candidate)

        then:
        sex == expectedSex

        where:
        candidate     | expectedSex
        '210766-1020' | Sex.FEMALE
        '210766-1021' | Sex.MALE
        '010101-0101' | Sex.MALE
        '101000-9990' | Sex.FEMALE
    }

    @Unroll
    def "defaults to unknown sex if trying to calculate sex on CPR number with wrong length"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        def sex = service.calculateSex(candidate)

        then:
        sex == expectedSex

        where:
        candidate | expectedSex
        '1234'    | Sex.UNKNOWN
        ''        | Sex.UNKNOWN
        null      | Sex.UNKNOWN
    }

    @Unroll
    def "returns unknown sex when patient identification is not CPR number"() {
        given:
        grailsApplication.config.patient.identification.isCpr = false

        when:
        def sex = service.calculateSex(candidate)

        then:
        sex == expectedSex

        where:
        candidate          | expectedSex
        '210766-1020'      | Sex.UNKNOWN
        '0101010101234234' | Sex.UNKNOWN
        'foobar'           | Sex.UNKNOWN
    }

    def "CPR number can be properly formatted"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        def formatted = service.formatForDisplay('2411451220')

        then:
        formatted == '241145-1220'
    }

    def "CPR number with wrong length will not be formatted"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        def formatted = service.formatForDisplay('241145122') // too short

        then:
        formatted == '241145122'
    }

    def "non-CPR number identification will not be formatted"() {
        given:
        grailsApplication.config.patient.identification.isCpr = false

        when:
        def formatted = service.formatForDisplay('2411451220')

        then:
        formatted == '2411451220'
    }

    def "will setup validation for CPR identification"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        service.setupValidation()

        then:
        new CreatePatientCommand().respondsTo("validateIdentification")
        new Patient().respondsTo("validateIdentification")
    }

    def "will setup validation for non-CPR identification"() {
        given:
        grailsApplication.config.patient.identification.isCpr = false

        when:
        service.setupValidation()

        then:
        new CreatePatientCommand().respondsTo("validateIdentification")
        new Patient().respondsTo("validateIdentification")
    }

    def "can remove dashes and spaces for CPR identification"() {
        given:
        grailsApplication.config.patient.identification.isCpr = true

        when:
        def formatted1 = service.formatForStorage('120566-1245')
        def formatted2 = service.formatForStorage('120566 1245')

        then:
        formatted1 == '1205661245'
        formatted2 == '1205661245'
    }

    def "will not remove spaces and dashes for non-CPR identification"() {
        given:
        grailsApplication.config.patient.identification.isCpr = false

        when:
        def formatted1 = service.formatForStorage('120566-1245')
        def formatted2 = service.formatForStorage('120566 1245')

        then:
        formatted1 == '120566-1245'
        formatted2 == '120566 1245'
    }

    def "null identification number will be ignored when formatting for storage"() {
        when:
        def formatted = service.formatForStorage(null)

        then:
        formatted == null
    }

    def "null identification number will be ignored when formatting for display"() {
        when:
        def formatted = service.formatForDisplay(null)

        then:
        formatted == null
    }
}
