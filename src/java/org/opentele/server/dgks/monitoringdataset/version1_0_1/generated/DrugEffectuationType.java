
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DrugEffectuationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DrugEffectuationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2008/06/01}EffectuationIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2008/06/01}DrugName"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2008/06/01}DrugFormText"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2008/06/01}DrugStrengthText"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2009/01/01}DosageFreeText"/>
 *         &lt;element name="AccordingToNeed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2009/01/01}IndicationFreeText" minOccurs="0"/>
 *         &lt;element ref="{http://www.dkma.dk/medicinecard/xml.schema/2008/06/01}ATCCode"/>
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
@XmlType(name = "DrugEffectuationType", propOrder = {
    "effectuationIdentifier",
    "createdDateTime",
    "drugName",
    "drugFormText",
    "drugStrengthText",
    "dosageFreeText",
    "accordingToNeed",
    "indicationFreeText",
    "atcCode",
    "createdBy"
})
public class DrugEffectuationType {

    @XmlElement(name = "EffectuationIdentifier", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01")
    protected long effectuationIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "DrugName", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01", required = true)
    protected String drugName;
    @XmlElement(name = "DrugFormText", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01", required = true)
    protected String drugFormText;
    @XmlElement(name = "DrugStrengthText", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01", required = true)
    protected String drugStrengthText;
    @XmlElement(name = "DosageFreeText", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2009/01/01", required = true)
    protected String dosageFreeText;
    @XmlElement(name = "AccordingToNeed")
    protected Boolean accordingToNeed;
    @XmlElement(name = "IndicationFreeText", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2009/01/01")
    protected String indicationFreeText;
    @XmlElement(name = "ATCCode", namespace = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01", required = true)
    protected String atcCode;
    @XmlElement(name = "CreatedBy", required = true)
    protected String createdBy;

    /**
     * Gets the value of the effectuationIdentifier property.
     * 
     */
    public long getEffectuationIdentifier() {
        return effectuationIdentifier;
    }

    /**
     * Sets the value of the effectuationIdentifier property.
     * 
     */
    public void setEffectuationIdentifier(long value) {
        this.effectuationIdentifier = value;
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
     * Gets the value of the drugName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     * Sets the value of the drugName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrugName(String value) {
        this.drugName = value;
    }

    /**
     * Gets the value of the drugFormText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrugFormText() {
        return drugFormText;
    }

    /**
     * Sets the value of the drugFormText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrugFormText(String value) {
        this.drugFormText = value;
    }

    /**
     * Gets the value of the drugStrengthText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrugStrengthText() {
        return drugStrengthText;
    }

    /**
     * Sets the value of the drugStrengthText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrugStrengthText(String value) {
        this.drugStrengthText = value;
    }

    /**
     * Gets the value of the dosageFreeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDosageFreeText() {
        return dosageFreeText;
    }

    /**
     * Sets the value of the dosageFreeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDosageFreeText(String value) {
        this.dosageFreeText = value;
    }

    /**
     * Gets the value of the accordingToNeed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccordingToNeed() {
        return accordingToNeed;
    }

    /**
     * Sets the value of the accordingToNeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccordingToNeed(Boolean value) {
        this.accordingToNeed = value;
    }

    /**
     * Gets the value of the indicationFreeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicationFreeText() {
        return indicationFreeText;
    }

    /**
     * Sets the value of the indicationFreeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicationFreeText(String value) {
        this.indicationFreeText = value;
    }

    /**
     * Gets the value of the atcCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getATCCode() {
        return atcCode;
    }

    /**
     * Sets the value of the atcCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setATCCode(String value) {
        this.atcCode = value;
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
