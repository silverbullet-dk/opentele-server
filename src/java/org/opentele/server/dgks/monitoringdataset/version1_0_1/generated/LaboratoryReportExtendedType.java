
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LaboratoryReportExtendedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LaboratoryReportExtendedType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CreatedDateTime"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}AnalysisText"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultText"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultEncodingIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultOperatorIdentifier" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultUnitText"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultAbnormalIdentifier" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultMinimumText" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ResultMaximumText" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}ResultTypeOfInterval" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}NationalSampleIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}IupacIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ProducerOfLabResult"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}Instrument" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasurementTransferredBy"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasurementLocation"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasuringDataClassification" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasurementDuration" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasurementScheduled"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}HealthCareProfessionalComment" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}MeasuringCircumstances" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaboratoryReportExtendedType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
    "uuidIdentifier",
    "createdDateTime",
    "analysisText",
    "resultText",
    "resultEncodingIdentifier",
    "resultOperatorIdentifier",
    "resultUnitText",
    "resultAbnormalIdentifier",
    "resultMinimumText",
    "resultMaximumText",
    "resultTypeOfInterval",
    "nationalSampleIdentifier",
    "iupacIdentifier",
    "producerOfLabResult",
    "instrument",
    "measurementTransferredBy",
    "measurementLocation",
    "measuringDataClassification",
    "measurementDuration",
    "measurementScheduled",
    "healthCareProfessionalComment",
    "measuringCircumstances"
})
public class LaboratoryReportExtendedType {

    @XmlElement(name = "UuidIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "AnalysisText", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String analysisText;
    @XmlElement(name = "ResultText", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String resultText;
    @XmlElement(name = "ResultEncodingIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected EncodingIdentifierType resultEncodingIdentifier;
    @XmlElement(name = "ResultOperatorIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0")
    protected OperatorIdentifierType resultOperatorIdentifier;
    @XmlElement(name = "ResultUnitText", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String resultUnitText;
    @XmlElement(name = "ResultAbnormalIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0")
    protected AbnormalIdentifierType resultAbnormalIdentifier;
    @XmlElement(name = "ResultMinimumText", namespace = "urn:oio:medcom:chronicdataset:1.0.0")
    protected String resultMinimumText;
    @XmlElement(name = "ResultMaximumText", namespace = "urn:oio:medcom:chronicdataset:1.0.0")
    protected String resultMaximumText;
    @XmlElement(name = "ResultTypeOfInterval")
    protected String resultTypeOfInterval;
    @XmlElement(name = "NationalSampleIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String nationalSampleIdentifier;
    @XmlElement(name = "IupacIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String iupacIdentifier;
    @XmlElement(name = "ProducerOfLabResult", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected ProducerOfLabResultType producerOfLabResult;
    @XmlElement(name = "Instrument")
    protected InstrumentType instrument;
    @XmlElement(name = "MeasurementTransferredBy", required = true)
    protected MeasurementTransferredByType measurementTransferredBy;
    @XmlElement(name = "MeasurementLocation", required = true)
    protected MeasurementLocationType measurementLocation;
    @XmlElement(name = "MeasuringDataClassification")
    protected MeasuringDataClassificationType measuringDataClassification;
    @XmlElement(name = "MeasurementDuration")
    protected String measurementDuration;
    @XmlElement(name = "MeasurementScheduled", required = true)
    protected MeasurementScheduledType measurementScheduled;
    @XmlElement(name = "HealthCareProfessionalComment")
    protected String healthCareProfessionalComment;
    @XmlElement(name = "MeasuringCircumstances")
    protected String measuringCircumstances;

    /**
     * Gets the value of the uuidIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuidIdentifier() {
        return uuidIdentifier;
    }

    /**
     * Sets the value of the uuidIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuidIdentifier(String value) {
        this.uuidIdentifier = value;
    }

    /**
     * Gets the value of the createdDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Sets the value of the createdDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedDateTime(XMLGregorianCalendar value) {
        this.createdDateTime = value;
    }

    /**
     * Gets the value of the analysisText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisText() {
        return analysisText;
    }

    /**
     * Sets the value of the analysisText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisText(String value) {
        this.analysisText = value;
    }

    /**
     * Gets the value of the resultText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultText() {
        return resultText;
    }

    /**
     * Sets the value of the resultText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultText(String value) {
        this.resultText = value;
    }

    /**
     * Gets the value of the resultEncodingIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link EncodingIdentifierType }
     *     
     */
    public EncodingIdentifierType getResultEncodingIdentifier() {
        return resultEncodingIdentifier;
    }

    /**
     * Sets the value of the resultEncodingIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncodingIdentifierType }
     *     
     */
    public void setResultEncodingIdentifier(EncodingIdentifierType value) {
        this.resultEncodingIdentifier = value;
    }

    /**
     * Gets the value of the resultOperatorIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link OperatorIdentifierType }
     *     
     */
    public OperatorIdentifierType getResultOperatorIdentifier() {
        return resultOperatorIdentifier;
    }

    /**
     * Sets the value of the resultOperatorIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatorIdentifierType }
     *     
     */
    public void setResultOperatorIdentifier(OperatorIdentifierType value) {
        this.resultOperatorIdentifier = value;
    }

    /**
     * Gets the value of the resultUnitText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultUnitText() {
        return resultUnitText;
    }

    /**
     * Sets the value of the resultUnitText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultUnitText(String value) {
        this.resultUnitText = value;
    }

    /**
     * Gets the value of the resultAbnormalIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link AbnormalIdentifierType }
     *     
     */
    public AbnormalIdentifierType getResultAbnormalIdentifier() {
        return resultAbnormalIdentifier;
    }

    /**
     * Sets the value of the resultAbnormalIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbnormalIdentifierType }
     *     
     */
    public void setResultAbnormalIdentifier(AbnormalIdentifierType value) {
        this.resultAbnormalIdentifier = value;
    }

    /**
     * Gets the value of the resultMinimumText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMinimumText() {
        return resultMinimumText;
    }

    /**
     * Sets the value of the resultMinimumText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMinimumText(String value) {
        this.resultMinimumText = value;
    }

    /**
     * Gets the value of the resultMaximumText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMaximumText() {
        return resultMaximumText;
    }

    /**
     * Sets the value of the resultMaximumText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMaximumText(String value) {
        this.resultMaximumText = value;
    }

    /**
     * Gets the value of the resultTypeOfInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultTypeOfInterval() {
        return resultTypeOfInterval;
    }

    /**
     * Sets the value of the resultTypeOfInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultTypeOfInterval(String value) {
        this.resultTypeOfInterval = value;
    }

    /**
     * Gets the value of the nationalSampleIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalSampleIdentifier() {
        return nationalSampleIdentifier;
    }

    /**
     * Sets the value of the nationalSampleIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalSampleIdentifier(String value) {
        this.nationalSampleIdentifier = value;
    }

    /**
     * Gets the value of the iupacIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIupacIdentifier() {
        return iupacIdentifier;
    }

    /**
     * Sets the value of the iupacIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIupacIdentifier(String value) {
        this.iupacIdentifier = value;
    }

    /**
     * Gets the value of the producerOfLabResult property.
     * 
     * @return
     *     possible object is
     *     {@link ProducerOfLabResultType }
     *     
     */
    public ProducerOfLabResultType getProducerOfLabResult() {
        return producerOfLabResult;
    }

    /**
     * Sets the value of the producerOfLabResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProducerOfLabResultType }
     *     
     */
    public void setProducerOfLabResult(ProducerOfLabResultType value) {
        this.producerOfLabResult = value;
    }

    /**
     * Gets the value of the instrument property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentType }
     *     
     */
    public InstrumentType getInstrument() {
        return instrument;
    }

    /**
     * Sets the value of the instrument property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentType }
     *     
     */
    public void setInstrument(InstrumentType value) {
        this.instrument = value;
    }

    /**
     * Gets the value of the measurementTransferredBy property.
     * 
     * @return
     *     possible object is
     *     {@link MeasurementTransferredByType }
     *     
     */
    public MeasurementTransferredByType getMeasurementTransferredBy() {
        return measurementTransferredBy;
    }

    /**
     * Sets the value of the measurementTransferredBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasurementTransferredByType }
     *     
     */
    public void setMeasurementTransferredBy(MeasurementTransferredByType value) {
        this.measurementTransferredBy = value;
    }

    /**
     * Gets the value of the measurementLocation property.
     * 
     * @return
     *     possible object is
     *     {@link MeasurementLocationType }
     *     
     */
    public MeasurementLocationType getMeasurementLocation() {
        return measurementLocation;
    }

    /**
     * Sets the value of the measurementLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasurementLocationType }
     *     
     */
    public void setMeasurementLocation(MeasurementLocationType value) {
        this.measurementLocation = value;
    }

    /**
     * Gets the value of the measuringDataClassification property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuringDataClassificationType }
     *     
     */
    public MeasuringDataClassificationType getMeasuringDataClassification() {
        return measuringDataClassification;
    }

    /**
     * Sets the value of the measuringDataClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuringDataClassificationType }
     *     
     */
    public void setMeasuringDataClassification(MeasuringDataClassificationType value) {
        this.measuringDataClassification = value;
    }

    /**
     * Gets the value of the measurementDuration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasurementDuration() {
        return measurementDuration;
    }

    /**
     * Sets the value of the measurementDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasurementDuration(String value) {
        this.measurementDuration = value;
    }

    /**
     * Gets the value of the measurementScheduled property.
     * 
     * @return
     *     possible object is
     *     {@link MeasurementScheduledType }
     *     
     */
    public MeasurementScheduledType getMeasurementScheduled() {
        return measurementScheduled;
    }

    /**
     * Sets the value of the measurementScheduled property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasurementScheduledType }
     *     
     */
    public void setMeasurementScheduled(MeasurementScheduledType value) {
        this.measurementScheduled = value;
    }

    /**
     * Gets the value of the healthCareProfessionalComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHealthCareProfessionalComment() {
        return healthCareProfessionalComment;
    }

    /**
     * Sets the value of the healthCareProfessionalComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHealthCareProfessionalComment(String value) {
        this.healthCareProfessionalComment = value;
    }

    /**
     * Gets the value of the measuringCircumstances property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasuringCircumstances() {
        return measuringCircumstances;
    }

    /**
     * Sets the value of the measuringCircumstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasuringCircumstances(String value) {
        this.measuringCircumstances = value;
    }

}
