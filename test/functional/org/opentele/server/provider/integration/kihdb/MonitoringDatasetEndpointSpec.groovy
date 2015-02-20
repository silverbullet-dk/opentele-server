package org.opentele.server.provider.integration.kihdb
//import org.opentele.server.util.XmlConverterUtil
import geb.spock.GebReportingSpec

class MonitoringDatasetEndpointSpec extends GebReportingSpec {
//    Logger log = LoggerFactory.getLogger(MonitoringDatasetEndpointSpec.class)
//
//    private final static String TEST_SSN = "2512484916"
//    private final static String URL = 'http://localhost:8090/kih_database/services/monitoringDataset'
//
//    TestDataUtil testDataUtil = new TestDataUtil()
//
//    RequestUtil requestUtil = new RequestUtil()
//
//    SosiUtil sosiUtil = new SosiUtil()
//    ObjectFactory objectFactory = new ObjectFactory()
//    SOSIFactory sosiFactory = sosiUtil.createSOSIFactory()
//
//    def soapClient = new SOAPClient(URL)
//    def response
//    def resultSet
//
//
//    def "Test upload of measurements"() {
//        when:
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
//
//        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
//
//        requestMsg.selfMonitoredSample = testDataUtil.loadTestdata(objectFactory)
//        def expectedResultSize = 0
//
//        for (sample in requestMsg.selfMonitoredSample) {
//            expectedResultSize += sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
//        }
//
//        Request request = sosiFactory.createNewRequest(false, "flow")
//
//        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
//        String soapRequest = requestUtil.generateRequest(reqWrap, request)
//
//        def lresponse
//
//        try {
//            response = soapClient.send(soapRequest)
//            lresponse = response
//            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list() // Extract the result set.
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//        }
//
//        then:
//        soapClient != null
//        200 == response.httpResponse.statusCode
//        expectedResultSize == resultSet.size()
//    }
//
//    def "Test no SSN"() {
//        when:
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
//
//        Request request = sosiFactory.createNewRequest(false,"flow")
//        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
//        String soapRequest = requestUtil.generateRequest(reqWrap, request)
//
//        try {
//            response = soapClient.send(soapRequest)
//            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list() // Extract the result set.
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//        }
//
//        then:
//        soapClient != null
//        200 == response.httpResponse.statusCode
//        BigInteger.ZERO == resultSet.size()
//    }
//
//    def "Test no data"() {
//        when:
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
//        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
//
//        Request request = sosiFactory.createNewRequest(false,"flow")
//        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
//        String soapRequest = requestUtil.generateRequest(reqWrap, request)
//
//        try {
//            response = soapClient.send(soapRequest)
//            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list() // Extract the result set.
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//        }
//
//        then:
//        soapClient != null
//        200 == response.httpResponse.statusCode
//        BigInteger.ZERO == resultSet.size()
//    }
//
//    def "Test empty UUID"() {
//        when:
//
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
//        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
//
//        def samples = testDataUtil.loadTestdata(objectFactory)
//        def sample =  samples.get(0)
//        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null
//
//        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
//        requestMsg .selfMonitoredSample.add(sample)
//
//        def expectedResultSize = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
//
//        Request request = sosiFactory.createNewRequest(false,"flow")
//        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
//        String soapRequest = requestUtil.generateRequest(reqWrap, request)
//
//        println "Sending: " + soapRequest
//
//        try {
//            response = soapClient.send(soapRequest)
//            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list() // Extract the result set.
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//        }
//
//        then:
//        soapClient != null
//        200 == response.httpResponse.statusCode
//        expectedResultSize == resultSet.size()
//    }
//
//    def "Test error On same LaboratoryReportUUID"() {
//        when:
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
//        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
//
//        def samples = testDataUtil.loadTestdata(objectFactory)
//        def sample =  samples.get(0)
//
//        log.debug "Sample: " + sample
//        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
//        requestMsg.selfMonitoredSample.add(sample)
//
//        def expectedResultSize = requestMsg.selfMonitoredSample.size()
//
//        Request request = sosiFactory.createNewRequest(false,"flow")
//        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
//        String soapRequest = requestUtil.generateRequest(reqWrap, request)
//
//        // Created the request
//        CreateMonitoringDatasetRequestMessage requestMsg2 = objectFactory.createCreateMonitoringDatasetRequestMessage()
//        requestMsg2.personCivilRegistrationIdentifier = TEST_SSN
//
//        def samples2 = testDataUtil.loadTestdata(objectFactory)
//        def sample2 =  samples.get(0)
//
//        requestMsg2.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
//        requestMsg2.selfMonitoredSample.add(sample2)
//
//        Request request2 = sosiFactory.createNewRequest(false,"flow")
//        reqWrap.createMonitoringDatasetRequestMessage = requestMsg2
//        String soapRequest2 = requestUtil.generateRequest(reqWrap, request2)
//
//        def booleanExceptionCaught = false
//        try {
//            log.info "Sending once!"
//            response = soapClient.send(soapRequest)
//            log.info "Sending twice!"
//            response = soapClient.send(soapRequest2)
//            log.debug "Response: " + response
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//            booleanExceptionCaught = true
//        }
//        then:
//        soapClient != null
//        booleanExceptionCaught == true
//        200 == response.httpResponse.statusCode
//    }
}
