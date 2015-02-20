package org.opentele.server.provider

import grails.test.spock.IntegrationSpec
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.provider.MeasurementTypeService

class MeasurementTypeServiceIntegrationSpec extends IntegrationSpec {
    MeasurementTypeService measurementTypeService

    def setup() {
    }

    def "when I retrieve types supporting thresholds, only relevant types (and never CTG) is returned"() {

        when:
        def types = measurementTypeService.getUnusedMeasurementTypesForThresholds(list)

        then:
        notContained.each {
            !types.contains(it)
        }

        where:
        list                                                           | notContained
        []                                                             | [MeasurementTypeName.CTG]
        [MeasurementTypeName.CRP, MeasurementTypeName.BLOOD_PRESSURE]  | [MeasurementTypeName.CTG, MeasurementTypeName.CRP, MeasurementTypeName.BLOOD_PRESSURE]

    }
}
