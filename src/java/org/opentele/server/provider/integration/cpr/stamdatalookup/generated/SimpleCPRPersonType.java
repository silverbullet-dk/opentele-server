
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SimpleCPRPersonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleCPRPersonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/}PersonNameStructure"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifier"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleCPRPersonType", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", propOrder = {
    "personNameStructure",
    "personCivilRegistrationIdentifier"
})
public class SimpleCPRPersonType {

    @XmlElement(name = "PersonNameStructure", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/", required = true)
    protected PersonNameStructureType personNameStructure;
    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", required = true)
    protected String personCivilRegistrationIdentifier;

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
     * Gets the value of the personCivilRegistrationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonCivilRegistrationIdentifier() {
        return personCivilRegistrationIdentifier;
    }

    /**
     * Sets the value of the personCivilRegistrationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonCivilRegistrationIdentifier(String value) {
        this.personCivilRegistrationIdentifier = value;
    }

}
