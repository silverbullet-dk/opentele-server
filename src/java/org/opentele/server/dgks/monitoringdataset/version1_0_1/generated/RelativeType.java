
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelativeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelativeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/}PersonNameStructure"/>
 *         &lt;element ref="{http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/}AddressPostal" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}PhoneNumberIdentifier" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}EmailAddressIdentifier" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelativeType", propOrder = {
    "personNameStructure",
    "addressPostal",
    "phoneNumberIdentifier",
    "emailAddressIdentifier"
})
public class RelativeType {

    @XmlElement(name = "PersonNameStructure", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/", required = true)
    protected PersonNameStructureType personNameStructure;
    @XmlElement(name = "AddressPostal", namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/")
    protected AddressPostalType addressPostal;
    @XmlElement(name = "PhoneNumberIdentifier")
    protected String phoneNumberIdentifier;
    @XmlElement(name = "EmailAddressIdentifier")
    protected String emailAddressIdentifier;

    /**
     * Gets the value of the personNameStructure property.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameStructureType }
     *     
     */
    public PersonNameStructureType getPersonNameStructure() {
        return personNameStructure;
    }

    /**
     * Sets the value of the personNameStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameStructureType }
     *     
     */
    public void setPersonNameStructure(PersonNameStructureType value) {
        this.personNameStructure = value;
    }

    /**
     * Gets the value of the addressPostal property.
     * 
     * @return
     *     possible object is
     *     {@link AddressPostalType }
     *     
     */
    public AddressPostalType getAddressPostal() {
        return addressPostal;
    }

    /**
     * Sets the value of the addressPostal property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressPostalType }
     *     
     */
    public void setAddressPostal(AddressPostalType value) {
        this.addressPostal = value;
    }

    /**
     * Gets the value of the phoneNumberIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumberIdentifier() {
        return phoneNumberIdentifier;
    }

    /**
     * Sets the value of the phoneNumberIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumberIdentifier(String value) {
        this.phoneNumberIdentifier = value;
    }

    /**
     * Gets the value of the emailAddressIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddressIdentifier() {
        return emailAddressIdentifier;
    }

    /**
     * Sets the value of the emailAddressIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddressIdentifier(String value) {
        this.emailAddressIdentifier = value;
    }

}
