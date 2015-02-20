
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegularCPRPersonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegularCPRPersonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/}SimpleCPRPerson"/>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/}PersonNameForAddressingName"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}PersonGenderCode"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/}PersonInformationProtectionIndicator"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/}PersonBirthDateStructure"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/}PersonCivilRegistrationStatusStructure"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegularCPRPersonType", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", propOrder = {
    "simpleCPRPerson",
    "personNameForAddressingName",
    "personGenderCode",
    "personInformationProtectionIndicator",
    "personBirthDateStructure",
    "personCivilRegistrationStatusStructure"
})
public class RegularCPRPersonType {

    @XmlElement(name = "SimpleCPRPerson", required = true)
    protected SimpleCPRPersonType simpleCPRPerson;
    @XmlElement(name = "PersonNameForAddressingName", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/", required = true)
    protected String personNameForAddressingName;
    @XmlElement(name = "PersonGenderCode", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", required = true)
    protected PersonGenderCodeType personGenderCode;
    @XmlElement(name = "PersonInformationProtectionIndicator", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/")
    protected boolean personInformationProtectionIndicator;
    @XmlElement(name = "PersonBirthDateStructure", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", required = true)
    protected PersonBirthDateStructureType personBirthDateStructure;
    @XmlElement(name = "PersonCivilRegistrationStatusStructure", required = true)
    protected PersonCivilRegistrationStatusStructureType personCivilRegistrationStatusStructure;

    /**
     * Gets the value of the simpleCPRPerson property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleCPRPersonType }
     *     
     */
    public SimpleCPRPersonType getSimpleCPRPerson() {
        return simpleCPRPerson;
    }

    /**
     * Sets the value of the simpleCPRPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleCPRPersonType }
     *     
     */
    public void setSimpleCPRPerson(SimpleCPRPersonType value) {
        this.simpleCPRPerson = value;
    }

    /**
     * Gets the value of the personNameForAddressingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonNameForAddressingName() {
        return personNameForAddressingName;
    }

    /**
     * Sets the value of the personNameForAddressingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonNameForAddressingName(String value) {
        this.personNameForAddressingName = value;
    }

    /**
     * Gets the value of the personGenderCode property.
     * 
     * @return
     *     possible object is
     *     {@link PersonGenderCodeType }
     *     
     */
    public PersonGenderCodeType getPersonGenderCode() {
        return personGenderCode;
    }

    /**
     * Sets the value of the personGenderCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonGenderCodeType }
     *     
     */
    public void setPersonGenderCode(PersonGenderCodeType value) {
        this.personGenderCode = value;
    }

    /**
     * Gets the value of the personInformationProtectionIndicator property.
     * 
     */
    public boolean isPersonInformationProtectionIndicator() {
        return personInformationProtectionIndicator;
    }

    /**
     * Sets the value of the personInformationProtectionIndicator property.
     * 
     */
    public void setPersonInformationProtectionIndicator(boolean value) {
        this.personInformationProtectionIndicator = value;
    }

    /**
     * Gets the value of the personBirthDateStructure property.
     * 
     * @return
     *     possible object is
     *     {@link PersonBirthDateStructureType }
     *     
     */
    public PersonBirthDateStructureType getPersonBirthDateStructure() {
        return personBirthDateStructure;
    }

    /**
     * Sets the value of the personBirthDateStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonBirthDateStructureType }
     *     
     */
    public void setPersonBirthDateStructure(PersonBirthDateStructureType value) {
        this.personBirthDateStructure = value;
    }

    /**
     * Gets the value of the personCivilRegistrationStatusStructure property.
     * 
     * @return
     *     possible object is
     *     {@link PersonCivilRegistrationStatusStructureType }
     *     
     */
    public PersonCivilRegistrationStatusStructureType getPersonCivilRegistrationStatusStructure() {
        return personCivilRegistrationStatusStructure;
    }

    /**
     * Sets the value of the personCivilRegistrationStatusStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonCivilRegistrationStatusStructureType }
     *     
     */
    public void setPersonCivilRegistrationStatusStructure(PersonCivilRegistrationStatusStructureType value) {
        this.personCivilRegistrationStatusStructure = value;
    }

}
