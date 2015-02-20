
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatutoryProvidedServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StatutoryProvidedServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccordingToSocialLegislation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AccordingToHealthLegislation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ProvidedByHealthCenter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatutoryProvidedServiceType", propOrder = {
    "accordingToSocialLegislation",
    "accordingToHealthLegislation",
    "providedByHealthCenter"
})
public class StatutoryProvidedServiceType {

    @XmlElement(name = "AccordingToSocialLegislation")
    protected Boolean accordingToSocialLegislation;
    @XmlElement(name = "AccordingToHealthLegislation")
    protected Boolean accordingToHealthLegislation;
    @XmlElement(name = "ProvidedByHealthCenter")
    protected Boolean providedByHealthCenter;

    /**
     * Gets the value of the accordingToSocialLegislation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccordingToSocialLegislation() {
        return accordingToSocialLegislation;
    }

    /**
     * Sets the value of the accordingToSocialLegislation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccordingToSocialLegislation(Boolean value) {
        this.accordingToSocialLegislation = value;
    }

    /**
     * Gets the value of the accordingToHealthLegislation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccordingToHealthLegislation() {
        return accordingToHealthLegislation;
    }

    /**
     * Sets the value of the accordingToHealthLegislation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccordingToHealthLegislation(Boolean value) {
        this.accordingToHealthLegislation = value;
    }

    /**
     * Gets the value of the providedByHealthCenter property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isProvidedByHealthCenter() {
        return providedByHealthCenter;
    }

    /**
     * Sets the value of the providedByHealthCenter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setProvidedByHealthCenter(Boolean value) {
        this.providedByHealthCenter = value;
    }

}
