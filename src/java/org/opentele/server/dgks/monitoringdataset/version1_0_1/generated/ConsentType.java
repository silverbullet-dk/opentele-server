
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ConsentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConsentDeclaredDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ConsentGivenIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsentType", propOrder = {
    "consentDeclaredDateTime",
    "consentGivenIndicator"
})
public class ConsentType {

    @XmlElement(name = "ConsentDeclaredDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consentDeclaredDateTime;
    @XmlElement(name = "ConsentGivenIndicator")
    protected boolean consentGivenIndicator;

    /**
     * Gets the value of the consentDeclaredDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsentDeclaredDateTime() {
        return consentDeclaredDateTime;
    }

    /**
     * Sets the value of the consentDeclaredDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsentDeclaredDateTime(XMLGregorianCalendar value) {
        this.consentDeclaredDateTime = value;
    }

    /**
     * Gets the value of the consentGivenIndicator property.
     * 
     */
    public boolean isConsentGivenIndicator() {
        return consentGivenIndicator;
    }

    /**
     * Sets the value of the consentGivenIndicator property.
     * 
     */
    public void setConsentGivenIndicator(boolean value) {
        this.consentGivenIndicator = value;
    }

}
