package org.opentele.server.provider.integration.milou

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.apache.commons.logging.Log
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Meter
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.MeasurementTypeName
import wslite.http.HTTPClient
import wslite.http.HTTPResponse
import wslite.soap.SOAPClient

import javax.xml.bind.DatatypeConverter
import java.util.zip.GZIPInputStream

@TestMixin(GrailsUnitTestMixin)
@TestFor(MilouWebServiceClientService)
class MilouWebServiceClientServiceTests {
    private static final String SOAP_FAULT = """\
<?xml version="1.0" encoding="UTF-8"?>
<env:Envelope xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <env:Header />
  <env:Body>
    <env:Fault>
    </env:Fault>
  </env:Body>
</env:Envelope>
"""
    private static final String SERVICE_URL = 'http://eriks-macbook-pro.local:8088/mockBasicHttpBinding_ICtgImport'
    def logControl, httpClientControl
    def milouRegistration

    void testCanSendCtgMeasurementToServer() {
        setupMocksForSuccessfulRequest()
        assert service.sendCtgMeasurement(buildCtgMeasurement())

        logControl.verify()
        httpClientControl.verify()

        assert milouRegistration.patientField.idField.text() == "0000000000"
        assert milouRegistration.patientField.nameField.firstField.text() == "Hans"
        assert milouRegistration.patientField.nameField.lastField.text() == "Hansen"
        assert milouRegistration.contactField.timeField.text().startsWith('2013-02-13')
        assert milouRegistration.locationField.roomField.nameField.text() == 'Hjemme'

        String data = unzip(milouRegistration.ctgField.text().decodeBase64())
        assert data.startsWith("""\
[0.0, 74.0, 0.0, 2]
[1.0, 75.0, 1.5, 3]
[0.0, 74.0, 0.0, 2]
""")

        milouRegistration.markersField.children().each {
            DatatypeConverter.parseDateTime(it.text())
        }
    }

    void testHandlesSoapFaultsByLoggingErrorAndReturningFalse() {
        setupMocks()
        logControl.demand.error { String message, Exception e ->
            assert message.startsWith('Error occured while trying to upload CTG measurement')
        }
        httpClientControl.demand.execute { request ->
            def result = new HTTPResponse()
            result.data = SOAP_FAULT.bytes
            result
        }

        assert !service.sendCtgMeasurement(buildCtgMeasurement())

        logControl.verify()
        httpClientControl.verify()
    }

    void testHandlesEmptySignalString() {
        setupMocksForSuccessfulRequest()
        assert service.sendCtgMeasurement(buildCtgMeasurementWithoutSignals())

        assert milouRegistration.markersField.children().empty
    }

    void testHandlesEmptyArraySignalString() {
        setupMocksForSuccessfulRequest()
        assert service.sendCtgMeasurement(buildCtgMeasurement(signals: '[]'))

        assert milouRegistration.markersField.children().empty
    }

    void testHandlesEmptyMeasurement() {
        setupMocksForSuccessfulRequest()
        assert service.sendCtgMeasurement(buildCtgMeasurement(fhr: '[]', mhr: '[]', toco: '[]', qfhr: '[]'))

        assert unzip(milouRegistration.ctgField.text().decodeBase64()) == ''
    }

    void setupMocks() {
        logControl = mockFor(Log)
        httpClientControl = mockFor(HTTPClient)
        service.log = logControl.createMock()
        service.milouSoapClient = new SOAPClient(SERVICE_URL, httpClientControl.createMock())
    }

    void setupMocksForSuccessfulRequest() {
        setupMocks()
        httpClientControl.demand.execute { request ->
            milouRegistration = new XmlSlurper().parseText(new String(request.data)).Body.ImportCtg.reg
            new HTTPResponse()
        }
    }

    private Measurement buildCtgMeasurement(Map values=[:]) {
        Measurement measurement = new Measurement()
        measurement.setStartTime(date('13', '02', '2013'))
        measurement.setEndTime(date('14', '02', '2013'))

        measurement.fhr = values.fhr ?: "[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]"
        measurement.mhr = values.mhr ?: "[74.0, 75.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0]"
        measurement.qfhr = values.qfhr ?: "[2, 3, 2, 2, 2, 2, 2, 2, 2, 2]"
        measurement.toco = values.toco ?: "[0.0, 1.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]"
        measurement.signals = values.signals ?: "[\"2012-05-16T13:24:44Z\", \"2012-05-16T13:30:44Z\", \"2012-05-16T13:31:49Z\", \"2012-05-16T13:33:49Z\"]"

        measurement.meter = new Meter(meterId: "Mit fine device")

        def measurementType = new MeasurementType()
        measurementType.name = MeasurementTypeName.CTG
        measurement.measurementType = measurementType

        measurement.patient = new Patient(cpr: '0000000000', firstName: 'Hans', lastName: 'Hansen')

        measurement
    }

    private Measurement buildCtgMeasurementWithoutSignals() {

        Measurement measurement = buildCtgMeasurement()
        measurement.signals = null
        measurement.meter = null

        measurement
    }

    private Date date(day, month, year) {
        Date.parse('dd-MM-yyyy', "${day}-${month}-${year}")
    }

    private def unzip(byte[] bytes){
        def sourceStream = new ByteArrayInputStream(bytes)
        def zipStream = new GZIPInputStream(sourceStream)
        def outputStream = new ByteArrayOutputStream()

        for(int nextByte = zipStream.read(); nextByte >= 0; nextByte = zipStream.read()) {
            outputStream.write(nextByte)
        }

        new String(outputStream.toByteArray(), "ISO-8859-1")
    }
}