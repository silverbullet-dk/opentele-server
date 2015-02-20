
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonLookupResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonLookupResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}PersonInformationStructure" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonLookupResponseType", namespace = "http://nsi.dk/2011/09/23/StamdataCpr/", propOrder = {
    "personInformationStructure"
})
public class PersonLookupResponseType {

    @XmlElement(name = "PersonInformationStructure", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/")
    protected List<PersonInformationStructureType> personInformationStructure;

    /**
     * Gets the value of the personInformationStructure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personInformationStructure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonInformationStructure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonInformationStructureType }
     * 
     * 
     */
    public List<PersonInformationStructureType> getPersonInformationStructure() {
        if (personInformationStructure == null) {
            personInformationStructure = new ArrayList<PersonInformationStructureType>();
        }
        return this.personInformationStructure;
    }

}
