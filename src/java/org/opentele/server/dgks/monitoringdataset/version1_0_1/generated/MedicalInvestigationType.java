
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MedicalInvestigationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MedicalInvestigationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ReferralDiagnosisCollection"/>
 *         &lt;element name="DesiredServiceFormattedText" type="{urn:oio:medcom:chronicdataset:1.0.0}FormattedTextType" minOccurs="0"/>
 *         &lt;element name="AnamnesisFormattedText" type="{urn:oio:medcom:chronicdataset:1.0.0}FormattedTextType" minOccurs="0"/>
 *         &lt;element name="CreatedByText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedicalInvestigationType", propOrder = {
    "uuidIdentifier",
    "createdDateTime",
    "referralDiagnosisCollection",
    "desiredServiceFormattedText",
    "anamnesisFormattedText",
    "createdByText"
})
public class MedicalInvestigationType {

    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "ReferralDiagnosisCollection", required = true)
    protected ReferralDiagnosisCollectionType referralDiagnosisCollection;
    @XmlElement(name = "DesiredServiceFormattedText")
    protected FormattedTextType desiredServiceFormattedText;
    @XmlElement(name = "AnamnesisFormattedText")
    protected FormattedTextType anamnesisFormattedText;
    @XmlElement(name = "CreatedByText", required = true)
    protected String createdByText;

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
     * Gets the value of the referralDiagnosisCollection property.
     * 
     * @return
     *     possible object is
     *     {@link ReferralDiagnosisCollectionType }
     *     
     */
    public ReferralDiagnosisCollectionType getReferralDiagnosisCollection() {
        return referralDiagnosisCollection;
    }

    /**
     * Sets the value of the referralDiagnosisCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferralDiagnosisCollectionType }
     *     
     */
    public void setReferralDiagnosisCollection(ReferralDiagnosisCollectionType value) {
        this.referralDiagnosisCollection = value;
    }

    /**
     * Gets the value of the desiredServiceFormattedText property.
     * 
     * @return
     *     possible object is
     *     {@link FormattedTextType }
     *     
     */
    public FormattedTextType getDesiredServiceFormattedText() {
        return desiredServiceFormattedText;
    }

    /**
     * Sets the value of the desiredServiceFormattedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormattedTextType }
     *     
     */
    public void setDesiredServiceFormattedText(FormattedTextType value) {
        this.desiredServiceFormattedText = value;
    }

    /**
     * Gets the value of the anamnesisFormattedText property.
     * 
     * @return
     *     possible object is
     *     {@link FormattedTextType }
     *     
     */
    public FormattedTextType getAnamnesisFormattedText() {
        return anamnesisFormattedText;
    }

    /**
     * Sets the value of the anamnesisFormattedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormattedTextType }
     *     
     */
    public void setAnamnesisFormattedText(FormattedTextType value) {
        this.anamnesisFormattedText = value;
    }

    /**
     * Gets the value of the createdByText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedByText() {
        return createdByText;
    }

    /**
     * Sets the value of the createdByText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedByText(String value) {
        this.createdByText = value;
    }

}
