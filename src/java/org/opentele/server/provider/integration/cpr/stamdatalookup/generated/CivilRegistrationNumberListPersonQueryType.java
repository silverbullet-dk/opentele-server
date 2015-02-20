
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CivilRegistrationNumberListPersonQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CivilRegistrationNumberListPersonQueryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CivilRegistrationNumber" type="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifierType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CivilRegistrationNumberListPersonQueryType", namespace = "http://nsi.dk/2011/09/23/StamdataCpr/", propOrder = {
    "civilRegistrationNumber"
})
public class CivilRegistrationNumberListPersonQueryType {

    @XmlElement(name = "CivilRegistrationNumber", namespace = "", required = true)
    protected List<String> civilRegistrationNumber;

    /**
     * Gets the value of the civilRegistrationNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the civilRegistrationNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCivilRegistrationNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCivilRegistrationNumber() {
        if (civilRegistrationNumber == null) {
            civilRegistrationNumber = new ArrayList<String>();
        }
        return this.civilRegistrationNumber;
    }

}
