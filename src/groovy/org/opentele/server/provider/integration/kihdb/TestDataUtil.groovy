package org.opentele.server.provider.integration.kihdb

import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.EncodingIdentifierType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.LaboratoryReportExtendedType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.ObjectFactory
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.SelfMonitoredSampleType
import org.opentele.server.provider.util.XmlConverterUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.SimpleDateFormat

class TestDataUtil {
    private static Logger log = LoggerFactory.getLogger(TestDataUtil.class)

//    protected IDCard createIDCard(SOSIFactory factory) {
//        String systemName = "DemoCPR";
//        String orgCVR = "12345678";
//        String orgName = "Fyns Amt";
//
//        CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, orgCVR, orgName);
//        AuthenticationLevel authenticationLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM;
//
//        IDCard idCard = factory.createNewSystemIDCard(systemName, careProvider, authenticationLevel, null,
//                null, factory.getCredentialVault().getSystemCredentialPair().getCertificate(), null);
//
//        return idCard;
//    }
//
//    protected Request createRequest(SOSIFactory factory) {
//        String flowID = null; // only necessary when multiple calls to service (session)
//        return factory.createNewRequest(false, flowID);
//    }

    /**
     * Loads Test data from file
     * @return
     */
    def loadTestdata(ObjectFactory objectFactory) {
        List<SelfMonitoredSampleType> retVal = new ArrayList<SelfMonitoredSampleType>()

        File f = new File("test/data/MedComKD testeksempel 1 version 1.0.1.xml")

        def sb = new StringBuffer()
        f.eachLine { line ->
            sb.append(line)
        }

        log.debug("Loading measurements XML")

        def sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")


        def elements = new XmlParser().parseText(sb.toString())
        def size = elements.SelfMonitoredSample.size()
        def basicNodes = elements.SelfMonitoredSample
        basicNodes.each { node ->
            SelfMonitoredSampleType t = objectFactory.createSelfMonitoredSampleType()

            def createdBy = node.CreatedByText

            t.createdByText = createdBy[0]?.value()?.getAt(0)
            t.laboratoryReportExtendedCollection = objectFactory.createLaboratoryReportExtendedCollectionType()

            def reports = node.LaboratoryReportExtendedCollection.LaboratoryReportExtended

            /*

            <LaboratoryReportExtended>
                <UuidIdentifier>b33be781-bf97-11e1-afa7-0800200c9a66</UuidIdentifier>
                <CreatedDateTime>2006-05-04T18:13:51.0Z</CreatedDateTime>
                <AnalysisText>FEV1</AnalysisText>
                <ResultText>3.2</ResultText>
                <ResultEncodingIdentifier>numeric</ResultEncodingIdentifier>
                <ResultOperatorIdentifier>less_than</ResultOperatorIdentifier>
                <ResultUnitText>Liter</ResultUnitText>
                <ResultAbnormalIdentifier>to_high</ResultAbnormalIdentifier>
                <ResultMinimumText>1</ResultMinimumText>
                <ResultMaximumText>3</ResultMaximumText>
                <ResultTypeOfInterval>therapeutic</ResultTypeOfInterval>
                <NationalSampleIdentifier>9999999999</NationalSampleIdentifier>
                <IupacIdentifier>MCS88015</IupacIdentifier>
                <ProducerOfLabResult>
                    <Identifier>Patient målt</Identifier>
                    <IdentifierCode>PST</IdentifierCode>
                </ProducerOfLabResult>
                <Instrument>
                    <MedComID>EPQ1225</MedComID>
                    <Manufacturer>AD Medical</Manufacturer>
                    <ProductType>Multifunction Kit</ProductType>
                    <Model> UA-767PTB-C </Model>
                    <SoftwareVersion>10.0.3452r45</SoftwareVersion>
                </Instrument>
                <MeasurementTransferredBy>automatic</MeasurementTransferredBy>
                <MeasurementLocation>home</MeasurementLocation>
                <MeasuringDataClassification>clinical</MeasuringDataClassification>
                <MeasurementDuration>120 sek</MeasurementDuration>
                <MeasurementScheduled>scheduled</MeasurementScheduled>
                <HealthCareProfessionalComment>Pt. havde meget svært ved at puste igennem idag</HealthCareProfessionalComment>
                <MeasuringCircumstances>Udblæsningen blev foretaget siddende på en stol lige efter morgenmaden</MeasuringCircumstances>
            </LaboratoryReportExtended>
            */



            reports.each { report ->
                LaboratoryReportExtendedType r = new LaboratoryReportExtendedType()

                def uuid = UUID.randomUUID()
                r.uuidIdentifier = uuid.toString()
                r.createdDateTime = XmlConverterUtil.getDateAsXml(sdf.parse(report.CreatedDateTime[0]?.value()?.getAt(0)))
                r.analysisText = report.AnalysisText[0]?.value()?.getAt(0)
                r.resultText = report.ResultText[0]?.value()?.getAt(0)
                r.resultEncodingIdentifier = EncodingIdentifierType.fromValue(report.ResultEncodingIdentifier[0]?.value()?.getAt(0))
                r.resultUnitText = report.ResultUnitText[0]?.value()?.getAt(0)
                r.nationalSampleIdentifier = report.NationalSampleIdentifier[0]?.value()?.getAt(0)
                r.iupacIdentifier = report.IupacIdentifier[0]?.value()?.getAt(0)
                r.resultTypeOfInterval = report.ResultTypeOfInterval[0]?.value()?.getAt(0)

                r.producerOfLabResult = objectFactory.createProducerOfLabResultType()
                def labProducerIdentifier = report.ProducerOfLabResult[0]?.Identifier[0]?.value()?.getAt(0)
                def labProducerIdentifierCode = report.ProducerOfLabResult[0]?.IdentifierCode[0]?.value()?.getAt(0)

                r.producerOfLabResult.identifier = labProducerIdentifier
                r.producerOfLabResult.identifierCode = labProducerIdentifierCode

                r.instrument = objectFactory.createInstrumentType()

                r.instrument.medComID = report.Instrument[0]?.MedComID[0]?.value()?.getAt(0)
                r.instrument.manufacturer = report.Instrument[0]?.Manufacturer[0]?.value()?.getAt(0)
                r.instrument.model = report.Instrument[0]?.Model[0]?.value()?.getAt(0)
                r.instrument.productType = report.Instrument[0]?.ProductType[0]?.value()?.getAt(0)
                r.instrument.softwareVersion = report.Instrument[0]?.SoftwareVersion[0]?.value()?.getAt(0)

                // Currently not handled - thus commented out
//                r.healthCareProfessionalComment = new FormattedTextType()
//                r.healthCareProfessionalComment = report?.HealthCareProfessionalComment[0].value()

//                r.measuringCircumstances = new FormattedTextType()
//                r.measuringCircumstances = report?.MeasuringCircumstances[0].value()

                t.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(r)
            }
            retVal.add(t)
        }

        return retVal
    }
}
