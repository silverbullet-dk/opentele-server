
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LaboratoryReportExtendedCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LaboratoryReportExtendedCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}LaboratoryReportExtended" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaboratoryReportExtendedCollectionType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
    "laboratoryReportExtended"
})
public class LaboratoryReportExtendedCollectionType {

    @XmlElement(name = "LaboratoryReportExtended", required = true)
    protected List<LaboratoryReportExtendedType> laboratoryReportExtended;

    /**
     * Gets the value of the laboratoryReportExtended property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the laboratoryReportExtended property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLaboratoryReportExtended().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LaboratoryReportExtendedType }
     * 
     * 
     */
    public List<LaboratoryReportExtendedType> getLaboratoryReportExtended() {
        if (laboratoryReportExtended == null) {
            laboratoryReportExtended = new ArrayList<LaboratoryReportExtendedType>();
        }
        return this.laboratoryReportExtended;
    }

}
