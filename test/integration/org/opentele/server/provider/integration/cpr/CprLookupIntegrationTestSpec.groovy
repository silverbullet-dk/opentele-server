package org.opentele.server.provider.integration.cpr

import grails.test.spock.IntegrationSpec

class CprLookupIntegrationTestSpec extends IntegrationSpec {

    private static final String TEST_CPR = "2512484916"
    def cprLookupService

    def "Call CPR service with valid CPR"() {
        when:
        def person = cprLookupService.getPersonDetails(TEST_CPR)

        then:
        person.civilRegistrationNumber != null
        person.firstName != null
        person.lastName != null
        person.address != null
        person.postalCode != null
        person.city != null
        person.sex != null
    }

    def "Call CPR service with invalid CPR"() {
        when:
        def person = cprLookupService.getPersonDetails("1111")

        then:
        person.hasErrors == true
        person.errorMessage.isEmpty() == false
    }

    def "Call CPR service with valid CPR but not used cpr"() {
        when:
        def person = cprLookupService.getPersonDetails("1212123434")

        then:
        person.hasErrors == false
        person.civilRegistrationNumber == null
        person.firstName == null
        person.lastName == null
        person.address == null
        person.postalCode == null
        person.city == null
        person.sex == null
    }
}
