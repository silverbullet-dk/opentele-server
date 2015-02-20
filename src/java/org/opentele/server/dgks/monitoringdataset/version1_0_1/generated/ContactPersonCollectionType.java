
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactPersonCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactPersonCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CountyContactPerson"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HospitalContactPerson"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactPersonCollectionType", propOrder = {
    "countyContactPersonOrHospitalContactPerson"
})
public class ContactPersonCollectionType {

    @XmlElementRefs({
        @XmlElementRef(name = "CountyContactPerson", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
        @XmlElementRef(name = "HospitalContactPerson", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class)
    })
    protected List<JAXBElement<ContactPersonType>> countyContactPersonOrHospitalContactPerson;

    /**
     * Gets the value of the countyContactPersonOrHospitalContactPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countyContactPersonOrHospitalContactPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountyContactPersonOrHospitalContactPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ContactPersonType }{@code >}
     * {@link JAXBElement }{@code <}{@link ContactPersonType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<ContactPersonType>> getCountyContactPersonOrHospitalContactPerson() {
        if (countyContactPersonOrHospitalContactPerson == null) {
            countyContactPersonOrHospitalContactPerson = new ArrayList<JAXBElement<ContactPersonType>>();
        }
        return this.countyContactPersonOrHospitalContactPerson;
    }

}
