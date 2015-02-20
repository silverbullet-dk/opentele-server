
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonalGoalResultCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonalGoalResultCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}PersonalGoalResult" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonalGoalResultCollectionType", propOrder = {
    "personalGoalResult"
})
public class PersonalGoalResultCollectionType {

    @XmlElement(name = "PersonalGoalResult", required = true)
    protected List<PersonalGoalResultType> personalGoalResult;

    /**
     * Gets the value of the personalGoalResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personalGoalResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonalGoalResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonalGoalResultType }
     * 
     * 
     */
    public List<PersonalGoalResultType> getPersonalGoalResult() {
        if (personalGoalResult == null) {
            personalGoalResult = new ArrayList<PersonalGoalResultType>();
        }
        return this.personalGoalResult;
    }

}
