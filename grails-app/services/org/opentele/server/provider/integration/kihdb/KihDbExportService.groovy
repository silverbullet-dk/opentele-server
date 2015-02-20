package org.opentele.server.provider.integration.kihdb

import org.opentele.server.model.Measurement

class KihDbExportService {

    def kihDbWebServiceClientService
    def kihDbMeasurementService
    def sosiService

    def exportToKihDatabase() {
        def sosiRequest = sosiService.createRequest()

        kihDbMeasurementService.measurementsToExport().each { Measurement measurement ->
            try {
                log.info("Exporting measurement (id:'${measurement.id}', type: ${measurement?.measurementType?.name}) to KIH DB")

                def measurementExported = kihDbWebServiceClientService.sendMeasurement(measurement, sosiRequest)
                if (measurementExported) {

                    log.info("Measurement (id:'${measurement.id}') was exported to KIH DB")
                    kihDbMeasurementService.markAsExported(measurement)
                }
            } catch (Exception ex) {
                log.info("Failed exporting Measurement (id:'${measurement.id}') to KIH DB!", ex)
            }
        }
    }
}
