package org.opentele.taglib

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ThresholdTagLib)
class ThresholdTagLibSpec extends Specification {

    @Unroll
    def "test that 0 thresholds are shown as 0.0()"() {

        when:
        def attrs = [field: null, level: "alertHigh", threshold: [name: "CRP", alertHigh: threshold ]]

        then:
        tagLib.formatThreshold(attrs) == expected

        where:
        threshold       | expected
        0.12f           | "0,1"
        0.0f            | "0,0"
        null            | "-"
    }
}