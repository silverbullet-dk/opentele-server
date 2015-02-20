
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HealthAndPreventionServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HealthAndPreventionServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceStatus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthAndPreventionServiceType", propOrder = {
    "healthAndPreventionServiceStatus"
})
public class HealthAndPreventionServiceType {

    @XmlElement(name = "HealthAndPreventionServiceStatus", required = true)
    protected HealthAndPreventionServiceStatusType healthAndPreventionServiceStatus;

    /**
     * Gets the value of the healthAndPreventionServiceStatus property.
     * 
     * @return
     *     possible object is
     *     {@link HealthAndPreventionServiceStatusType }
     *     
     */
    public HealthAndPreventionServiceStatusType getHealthAndPreventionServiceStatus() {
        return healthAndPreventionServiceStatus;
    }

    /**
     * Sets the value of the healthAndPreventionServiceStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link HealthAndPreventionServiceStatusType }
     *     
     */
    public void setHealthAndPreventionServiceStatus(HealthAndPreventionServiceStatusType value) {
        this.healthAndPreventionServiceStatus = value;
    }

}
