
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for HealthProfessionalNoteType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HealthProfessionalNoteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TitleText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ContentsFormattedText" type="{urn:oio:medcom:chronicdataset:1.0.0}FormattedTextType" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HealthCareAreaIdentifier"/>
 *         &lt;element name="CreatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthProfessionalNoteType", propOrder = {
    "uuidIdentifier",
    "createdDateTime",
    "titleText",
    "contentsFormattedText",
    "healthCareAreaIdentifier",
    "createdBy"
})
public class HealthProfessionalNoteType {

    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "TitleText")
    protected String titleText;
    @XmlElement(name = "ContentsFormattedText")
    protected FormattedTextType contentsFormattedText;
    @XmlElement(name = "HealthCareAreaIdentifier", required = true)
    protected HealthCareAreaIdentifierType healthCareAreaIdentifier;
    @XmlElement(name = "CreatedBy", required = true)
    protected String createdBy;

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
     * Gets the value of the titleText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleText() {
        return titleText;
    }

    /**
     * Sets the value of the titleText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleText(String value) {
        this.titleText = value;
    }

    /**
     * Gets the value of the contentsFormattedText property.
     * 
     * @return
     *     possible object is
     *     {@link FormattedTextType }
     *     
     */
    public FormattedTextType getContentsFormattedText() {
        return contentsFormattedText;
    }

    /**
     * Sets the value of the contentsFormattedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormattedTextType }
     *     
     */
    public void setContentsFormattedText(FormattedTextType value) {
        this.contentsFormattedText = value;
    }

    /**
     * Gets the value of the healthCareAreaIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link HealthCareAreaIdentifierType }
     *     
     */
    public HealthCareAreaIdentifierType getHealthCareAreaIdentifier() {
        return healthCareAreaIdentifier;
    }

    /**
     * Sets the value of the healthCareAreaIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link HealthCareAreaIdentifierType }
     *     
     */
    public void setHealthCareAreaIdentifier(HealthCareAreaIdentifierType value) {
        this.healthCareAreaIdentifier = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

}
