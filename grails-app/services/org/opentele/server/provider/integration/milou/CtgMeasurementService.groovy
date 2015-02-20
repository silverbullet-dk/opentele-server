package org.opentele.server.provider.integration.milou

import org.opentele.server.model.Measurement
import org.opentele.server.core.model.types.MeasurementTypeName

class CtgMeasurementService {
    def ctgMeasurementsToExport() {

        return Measurement.where {
            exported == false
            measurementType.name == MeasurementTypeName.CTG
        }.list()
    }

    def markAsExported(Measurement ctgMeasurement) {
        ctgMeasurement.exported = true
        ctgMeasurement.save(failOnError: true)
    }
}
