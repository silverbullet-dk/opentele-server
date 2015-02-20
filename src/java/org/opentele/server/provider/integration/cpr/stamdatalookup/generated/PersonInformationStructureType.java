
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Informationer i CPR-registret omkring en person.
 * 
 * <p>Java class for PersonInformationStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonInformationStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurrentPersonCivilRegistrationIdentifier" type="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifierType" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/}RegularCPRPerson"/>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PersonAddressStructure"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonInformationStructureType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "currentPersonCivilRegistrationIdentifier",
    "regularCPRPerson",
    "personAddressStructure"
})
public class PersonInformationStructureType {

    @XmlElement(name = "CurrentPersonCivilRegistrationIdentifier")
    protected String currentPersonCivilRegistrationIdentifier;
    @XmlElement(name = "RegularCPRPerson", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", required = true)
    protected RegularCPRPersonType regularCPRPerson;
    @XmlElement(name = "PersonAddressStructure", required = true)
    protected PersonAddressStructureType personAddressStructure;

    /**
     * Gets the value of the currentPersonCivilRegistrationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentPersonCivilRegistrationIdentifier() {
        return currentPersonCivilRegistrationIdentifier;
    }

    /**
     * Sets the value of the currentPersonCivilRegistrationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentPersonCivilRegistrationIdentifier(String value) {
        this.currentPersonCivilRegistrationIdentifier = value;
    }

    /**
     * Informationer vedr. identifikation af personen
     * 
     * @return
     *     possible object is
     *     {@link RegularCPRPersonType }
     *     
     */
    public RegularCPRPersonType getRegularCPRPerson() {
        return regularCPRPerson;
    }

    /**
     * Sets the value of the regularCPRPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegularCPRPersonType }
     *     
     */
    public void setRegularCPRPerson(RegularCPRPersonType value) {
        this.regularCPRPerson = value;
    }

    /**
     * Personens postadresse
     * 
     * @return
     *     possible object is
     *     {@link PersonAddressStructureType }
     *     
     */
    public PersonAddressStructureType getPersonAddressStructure() {
        return personAddressStructure;
    }

    /**
     * Sets the value of the personAddressStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonAddressStructureType }
     *     
     */
    public void setPersonAddressStructure(PersonAddressStructureType value) {
        this.personAddressStructure = value;
    }

}
