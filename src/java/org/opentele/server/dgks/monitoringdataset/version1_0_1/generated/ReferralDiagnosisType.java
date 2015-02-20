
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReferralDiagnosisType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferralDiagnosisType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}DiagnosisClassificationIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}DiagnosisIdentifier"/>
 *         &lt;element name="DescriptionText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferralDiagnosisType", propOrder = {
    "diagnosisClassificationIdentifier",
    "diagnosisIdentifier",
    "descriptionText"
})
public class ReferralDiagnosisType {

    @XmlElement(name = "DiagnosisClassificationIdentifier", required = true)
    protected DiagnosisClassificationIdentifierType diagnosisClassificationIdentifier;
    @XmlElement(name = "DiagnosisIdentifier", required = true)
    protected String diagnosisIdentifier;
    @XmlElement(name = "DescriptionText")
    protected String descriptionText;

    /**
     * Gets the value of the diagnosisClassificationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link DiagnosisClassificationIdentifierType }
     *     
     */
    public DiagnosisClassificationIdentifierType getDiagnosisClassificationIdentifier() {
        return diagnosisClassificationIdentifier;
    }

    /**
     * Sets the value of the diagnosisClassificationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagnosisClassificationIdentifierType }
     *     
     */
    public void setDiagnosisClassificationIdentifier(DiagnosisClassificationIdentifierType value) {
        this.diagnosisClassificationIdentifier = value;
    }

    /**
     * Gets the value of the diagnosisIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosisIdentifier() {
        return diagnosisIdentifier;
    }

    /**
     * Sets the value of the diagnosisIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosisIdentifier(String value) {
        this.diagnosisIdentifier = value;
    }

    /**
     * Gets the value of the descriptionText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionText() {
        return descriptionText;
    }

    /**
     * Sets the value of the descriptionText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionText(String value) {
        this.descriptionText = value;
    }

}
