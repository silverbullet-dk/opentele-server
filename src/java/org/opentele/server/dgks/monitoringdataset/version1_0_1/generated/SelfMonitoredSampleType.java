
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelfMonitoredSampleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SelfMonitoredSampleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}LaboratoryReportExtendedCollection"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CreatedByText"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelfMonitoredSampleType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
    "laboratoryReportExtendedCollection",
    "createdByText"
})
public class SelfMonitoredSampleType {

    @XmlElement(name = "LaboratoryReportExtendedCollection", required = true)
    protected LaboratoryReportExtendedCollectionType laboratoryReportExtendedCollection;
    @XmlElement(name = "CreatedByText", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String createdByText;

    /**
     * Gets the value of the laboratoryReportExtendedCollection property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportExtendedCollectionType }
     *     
     */
    public LaboratoryReportExtendedCollectionType getLaboratoryReportExtendedCollection() {
        return laboratoryReportExtendedCollection;
    }

    /**
     * Sets the value of the laboratoryReportExtendedCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportExtendedCollectionType }
     *     
     */
    public void setLaboratoryReportExtendedCollection(LaboratoryReportExtendedCollectionType value) {
        this.laboratoryReportExtendedCollection = value;
    }

    /**
     * Gets the value of the createdByText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedByText() {
        return createdByText;
    }

    /**
     * Sets the value of the createdByText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedByText(String value) {
        this.createdByText = value;
    }

}
