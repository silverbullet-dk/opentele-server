package org.opentele.builders

import org.opentele.server.model.MeasurementType
import org.opentele.server.core.model.types.MeasurementTypeName

class MeasurementTypeBuilder {
    private MeasurementTypeName measurementTypeName

    MeasurementType build() {
        def result = new MeasurementType(name: measurementTypeName)
        result.save(failOnError: true)
        result
    }

    def ofType(MeasurementTypeName measurementTypeName) {
        this.measurementTypeName = measurementTypeName
        this
    }
}
