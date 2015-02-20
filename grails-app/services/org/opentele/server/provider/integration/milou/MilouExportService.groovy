package org.opentele.server.provider.integration.milou

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

import org.opentele.server.model.Measurement

class MilouExportService {


    def ctgMeasurementService
    def milouWebServiceClientService


    def exportToMilou() {
        ctgMeasurementService.ctgMeasurementsToExport().each { Measurement measurement ->

            try {

                def measurementExported = milouWebServiceClientService.sendCtgMeasurement(measurement)
                if (measurementExported) {
                    log.info("CTG Measurement (id:'${measurement.id}') was exported")
                    ctgMeasurementService.markAsExported(measurement)
                }
            } catch (Exception ex) {
                log.info("Failed exporting CTG Measurement (id:'${measurement.id}')", ex)
            }
        }
    }
}
