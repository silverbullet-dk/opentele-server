package org.opentele.server.provider.integration.cpr

import spock.lang.Specification
import spock.lang.Unroll

class CPRPersonSpec extends Specification {

    @Unroll
    def "Test null in middleName does not show up"() {
        when:
        def person = new CPRPerson(firstName: firstName, middleName: middleName, lastName: "No ne")

        then:
        result.equals(person.getFirstNames())

        where:
        result                        | firstName | middleName
        "John"                        | "John"    | null
        "John"                        | "John"    | ""
        "John"                        | "John"    | "     "
        "John Doe"                    | "John"    | "Doe"
        "Jane X."                     | "Jane"    | "X."
    }
}