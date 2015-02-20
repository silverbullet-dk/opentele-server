package org.opentele.taglib

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ToolTipTagLib)
class ToolTipTagLibSpec extends Specification {

    @Unroll
    def "tooltip label misses attributes an exception is thrown"() {
        when:
        tagLib.label(tooltip: tooltip, message: message, "")

        then:
        def e = thrown(GrailsTagException)
        e.message == thrownMessage

        where:
        tooltip | message | thrownMessage
        null    | null    | "Missing [tooltip] or [message] attribute on [tt:label]"
        "abc"   | "abc"   | "Can only specify [tooltip] or [message] attribute on [tt:label]"
    }

    @Unroll
    def "tooltip label output is correct with either tooltip or message attribute"() {
        expect:
        tagLib.label(tooltip: tooltip, message: message, { body }) == output

        where:
        tooltip   | message        | body   | output
        null      | "some.message" | ""     | '<label data-tooltip="some.message" ></label>'
        "tooltip" | null           | ""     | '<label data-tooltip="tooltip" ></label>'
        null      | "some.message" | "body" | '<label data-tooltip="some.message" >body</label>'
        "tooltip" | null           | "body" | '<label data-tooltip="tooltip" >body</label>'

    }
}
