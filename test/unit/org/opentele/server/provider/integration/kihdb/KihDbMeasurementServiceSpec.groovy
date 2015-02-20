package org.opentele.server.provider.integration.kihdb

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.core.model.types.GlucoseInUrineValue
import org.opentele.server.core.model.types.MeasurementTypeName
import spock.lang.Specification

@TestFor(KihDbMeasurementService)
@Build([Measurement, MeasurementType])
class KihDbMeasurementServiceSpec extends Specification {

    def "the measurements to export are not already exported and of the specified types"() {
        setup:
        def values = [
                "CTG": [fhr: "fhr"],
                "HEMOGLOBIN": [value: 0],
                "TEMPERATURE": [value: 0],
                "URINE_GLUCOSE": [glucoseInUrine: GlucoseInUrineValue.NEGATIVE]
        ]

        def notToBeExported = Measurement.notToBeExportedMeasurementTypes.collect { typeName ->
            def type = MeasurementType.build(name: typeName)
            def value = [exportedToKih: false, measurementType: type]
            value.putAll(values.get(typeName.name()))
            Measurement.build(value)
        }

        def bloodSugarType = MeasurementType.build(name: MeasurementTypeName.BLOODSUGAR)
        def bloodSugar = Measurement.build(exportedToKih: false, measurementType: bloodSugarType, value: 0)

        def alreadyExported = Measurement.build(exportedToKih: true, measurementType: bloodSugarType, value: 0)
        notToBeExported << alreadyExported

        when:
        def exported = service.measurementsToExport()

        then:
        notToBeExported.size() >= 2
        exported.contains(bloodSugar)
        notToBeExported.every { !exported.contains(it) }
    }
}
