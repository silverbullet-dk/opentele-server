
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Informationer paa person og dennes laege der er registreret i CPR-registret.
 *             
 * 
 * <p>Java class for PersonWithHealthCareInformationStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonWithHealthCareInformationStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PersonInformationStructure"/>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PersonHealthCareInformationStructure"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonWithHealthCareInformationStructureType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "personInformationStructure",
    "personHealthCareInformationStructure"
})
public class PersonWithHealthCareInformationStructureType {

    @XmlElement(name = "PersonInformationStructure", required = true)
    protected PersonInformationStructureType personInformationStructure;
    @XmlElement(name = "PersonHealthCareInformationStructure", required = true)
    protected PersonHealthCareInformationStructureType personHealthCareInformationStructure;

    /**
     * Informationer vedr. personen selv.
     * 
     * @return
     *     possible object is
     *     {@link PersonInformationStructureType }
     *     
     */
    public PersonInformationStructureType getPersonInformationStructure() {
        return personInformationStructure;
    }

    /**
     * Sets the value of the personInformationStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonInformationStructureType }
     *     
     */
    public void setPersonInformationStructure(PersonInformationStructureType value) {
        this.personInformationStructure = value;
    }

    /**
     * Informationer vedr. personens laege og sygesikringsgruppe.
     * 
     * @return
     *     possible object is
     *     {@link PersonHealthCareInformationStructureType }
     *     
     */
    public PersonHealthCareInformationStructureType getPersonHealthCareInformationStructure() {
        return personHealthCareInformationStructure;
    }

    /**
     * Sets the value of the personHealthCareInformationStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonHealthCareInformationStructureType }
     *     
     */
    public void setPersonHealthCareInformationStructure(PersonHealthCareInformationStructureType value) {
        this.personHealthCareInformationStructure = value;
    }

}
