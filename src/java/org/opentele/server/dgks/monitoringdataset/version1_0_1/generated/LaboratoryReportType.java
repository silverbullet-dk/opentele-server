
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LaboratoryReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LaboratoryReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="AnalysisText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultEncodingIdentifier" type="{urn:oio:medcom:chronicdataset:1.0.0}EncodingIdentifierType"/>
 *         &lt;element name="ResultOperatorIdentifier" type="{urn:oio:medcom:chronicdataset:1.0.0}OperatorIdentifierType" minOccurs="0"/>
 *         &lt;element name="ResultUnitText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultAbnormalIdentifier" type="{urn:oio:medcom:chronicdataset:1.0.0}AbnormalIdentifierType" minOccurs="0"/>
 *         &lt;element name="ResultMinimumText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultMaximumText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NationalSampleIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IupacIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ProducerOfLabResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaboratoryReportType", propOrder = {
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
    "nationalSampleIdentifier",
    "iupacIdentifier",
    "producerOfLabResult"
})
public class LaboratoryReportType {

    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "AnalysisText", required = true)
    protected String analysisText;
    @XmlElement(name = "ResultText", required = true)
    protected String resultText;
    @XmlElement(name = "ResultEncodingIdentifier", required = true)
    protected EncodingIdentifierType resultEncodingIdentifier;
    @XmlElement(name = "ResultOperatorIdentifier")
    protected OperatorIdentifierType resultOperatorIdentifier;
    @XmlElement(name = "ResultUnitText", required = true)
    protected String resultUnitText;
    @XmlElement(name = "ResultAbnormalIdentifier")
    protected AbnormalIdentifierType resultAbnormalIdentifier;
    @XmlElement(name = "ResultMinimumText")
    protected String resultMinimumText;
    @XmlElement(name = "ResultMaximumText")
    protected String resultMaximumText;
    @XmlElement(name = "NationalSampleIdentifier", required = true)
    protected String nationalSampleIdentifier;
    @XmlElement(name = "IupacIdentifier", required = true)
    protected String iupacIdentifier;
    @XmlElement(name = "ProducerOfLabResult", required = true)
    protected ProducerOfLabResultType producerOfLabResult;

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

}
