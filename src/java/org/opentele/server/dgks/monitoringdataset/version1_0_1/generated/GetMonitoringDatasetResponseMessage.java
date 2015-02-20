
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}CitizenMonitoringDataset"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "citizenMonitoringDataset"
})
@XmlRootElement(name = "GetMonitoringDatasetResponseMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class GetMonitoringDatasetResponseMessage {

    @XmlElement(name = "CitizenMonitoringDataset", namespace = "urn:oio:medcom:chronicdataset:1.0.1", required = true)
    protected CitizenMonitoringDatasetType citizenMonitoringDataset;

    /**
     * Gets the value of the citizenMonitoringDataset property.
     * 
     * @return
     *     possible object is
     *     {@link CitizenMonitoringDatasetType }
     *     
     */
    public CitizenMonitoringDatasetType getCitizenMonitoringDataset() {
        return citizenMonitoringDataset;
    }

    /**
     * Sets the value of the citizenMonitoringDataset property.
     * 
     * @param value
     *     allowed object is
     *     {@link CitizenMonitoringDatasetType }
     *     
     */
    public void setCitizenMonitoringDataset(CitizenMonitoringDatasetType value) {
        this.citizenMonitoringDataset = value;
    }

}
