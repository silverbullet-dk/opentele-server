
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Sygesikringsgruppe for en person i det danske sundhedsvaesen.
 * 
 * <p>Java class for PersonPublicHealthInsuranceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonPublicHealthInsuranceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PublicHealthInsuranceGroupIdentifier"/>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PublicHealthInsuranceGroupStartDate" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonPublicHealthInsuranceType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "publicHealthInsuranceGroupIdentifier",
    "publicHealthInsuranceGroupStartDate"
})
public class PersonPublicHealthInsuranceType {

    @XmlElement(name = "PublicHealthInsuranceGroupIdentifier", required = true)
    protected PublicHealthInsuranceGroupIdentifierType publicHealthInsuranceGroupIdentifier;
    @XmlElement(name = "PublicHealthInsuranceGroupStartDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar publicHealthInsuranceGroupStartDate;

    /**
     * Sygesikringsgruppenummer.
     * 
     * @return
     *     possible object is
     *     {@link PublicHealthInsuranceGroupIdentifierType }
     *     
     */
    public PublicHealthInsuranceGroupIdentifierType getPublicHealthInsuranceGroupIdentifier() {
        return publicHealthInsuranceGroupIdentifier;
    }

    /**
     * Sets the value of the publicHealthInsuranceGroupIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicHealthInsuranceGroupIdentifierType }
     *     
     */
    public void setPublicHealthInsuranceGroupIdentifier(PublicHealthInsuranceGroupIdentifierType value) {
        this.publicHealthInsuranceGroupIdentifier = value;
    }

    /**
     * Dato for indtraedelse i denne gruppe.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPublicHealthInsuranceGroupStartDate() {
        return publicHealthInsuranceGroupStartDate;
    }

    /**
     * Sets the value of the publicHealthInsuranceGroupStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPublicHealthInsuranceGroupStartDate(XMLGregorianCalendar value) {
        this.publicHealthInsuranceGroupStartDate = value;
    }

}
