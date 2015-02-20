
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Informationer vedr. en persons laege og sygesikringsgruppe.
 * 
 * <p>Java class for PersonHealthCareInformationStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonHealthCareInformationStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}AssociatedGeneralPractitionerStructure"/>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PersonPublicHealthInsurance"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonHealthCareInformationStructureType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "associatedGeneralPractitionerStructure",
    "personPublicHealthInsurance"
})
public class PersonHealthCareInformationStructureType {

    @XmlElement(name = "AssociatedGeneralPractitionerStructure", required = true)
    protected AssociatedGeneralPractitionerStructureType associatedGeneralPractitionerStructure;
    @XmlElement(name = "PersonPublicHealthInsurance", required = true)
    protected PersonPublicHealthInsuranceType personPublicHealthInsurance;

    /**
     * Informationer vedr. personens laege.
     * 
     * @return
     *     possible object is
     *     {@link AssociatedGeneralPractitionerStructureType }
     *     
     */
    public AssociatedGeneralPractitionerStructureType getAssociatedGeneralPractitionerStructure() {
        return associatedGeneralPractitionerStructure;
    }

    /**
     * Sets the value of the associatedGeneralPractitionerStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssociatedGeneralPractitionerStructureType }
     *     
     */
    public void setAssociatedGeneralPractitionerStructure(AssociatedGeneralPractitionerStructureType value) {
        this.associatedGeneralPractitionerStructure = value;
    }

    /**
     * Informationer vedr. personens sygesikringsgruppe.
     * 
     * @return
     *     possible object is
     *     {@link PersonPublicHealthInsuranceType }
     *     
     */
    public PersonPublicHealthInsuranceType getPersonPublicHealthInsurance() {
        return personPublicHealthInsurance;
    }

    /**
     * Sets the value of the personPublicHealthInsurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonPublicHealthInsuranceType }
     *     
     */
    public void setPersonPublicHealthInsurance(PersonPublicHealthInsuranceType value) {
        this.personPublicHealthInsurance = value;
    }

}
