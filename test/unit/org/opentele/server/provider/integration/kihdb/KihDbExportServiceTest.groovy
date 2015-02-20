package org.opentele.server.provider.integration.kihdb

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import org.opentele.server.model.Measurement
import org.opentele.server.provider.sosi.SosiService

@TestFor(KihDbExportService)
@TestMixin(GrailsUnitTestMixin)
//@Mock(Measurement)
class KihDbExportServiceTest {
    def kihDbExportService
    def kihDbWebServiceClientServiceControl
    def kihDbMeasurementServiceControl
    def sosiServiceControl

    void setUp() {
        kihDbExportService = new KihDbExportService()
        kihDbWebServiceClientServiceControl = mockFor(KihDbWebServiceClientService)
        kihDbMeasurementServiceControl = mockFor(KihDbMeasurementService)
        sosiServiceControl = mockFor(SosiService)
    }

    void testExportsNewMeasurementsAndMarksThemAsExported() {
        sosiServiceControl.demand.createRequest { -> }

        def singleMeasurement = new Measurement()
        kihDbWebServiceClientServiceControl.demand.sendMeasurement { measurement, _ ->
            assert measurement == singleMeasurement
            return true
        }

        kihDbMeasurementServiceControl.demand.measurementsToExport { -> [singleMeasurement] }
        kihDbMeasurementServiceControl.demand.markAsExported { Measurement measurement ->
            assert measurement == singleMeasurement
        }

        verify()
    }

    void testDoesNotMarkMeasurementsAsExportedIfExportFails() {
        sosiServiceControl.demand.createRequest { -> }
        kihDbMeasurementServiceControl.demand.measurementsToExport { -> [new Measurement()]}
        kihDbWebServiceClientServiceControl.demand.sendMeasurement { _, __ ->
            return false
        }

        verify()
    }

    void testSosiRequestCreatedOncePerBatch()
    {
        //should be called precisely once 1..1
        def sosiRequest = mockFor(Object)
        sosiServiceControl.demand.createRequest(1..1) { -> sosiRequest }
        kihDbMeasurementServiceControl.demand.measurementsToExport { -> [new Measurement(), new Measurement()] }
        //reuse the sosi request
        kihDbWebServiceClientServiceControl.demand.sendMeasurement(2..2) { _, request ->
            assert request == sosiRequest
            return false
        }
        verify()
    }

    private void verify() {
        kihDbExportService.kihDbMeasurementService = kihDbMeasurementServiceControl.createMock()
        kihDbExportService.kihDbWebServiceClientService = kihDbWebServiceClientServiceControl.createMock()
        kihDbExportService.sosiService = sosiServiceControl.createMock()

        kihDbExportService.exportToKihDatabase()

        kihDbMeasurementServiceControl.verify()
        kihDbWebServiceClientServiceControl.verify()
        sosiServiceControl.verify()
    }
}
