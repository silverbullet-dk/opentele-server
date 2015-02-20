
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrentDrugEffectuationCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrentDrugEffectuationCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}DrugEffectuation" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrentDrugEffectuationCollectionType", propOrder = {
    "drugEffectuation"
})
public class CurrentDrugEffectuationCollectionType {

    @XmlElement(name = "DrugEffectuation", required = true)
    protected List<DrugEffectuationType> drugEffectuation;

    /**
     * Gets the value of the drugEffectuation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the drugEffectuation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDrugEffectuation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DrugEffectuationType }
     * 
     * 
     */
    public List<DrugEffectuationType> getDrugEffectuation() {
        if (drugEffectuation == null) {
            drugEffectuation = new ArrayList<DrugEffectuationType>();
        }
        return this.drugEffectuation;
    }

}
