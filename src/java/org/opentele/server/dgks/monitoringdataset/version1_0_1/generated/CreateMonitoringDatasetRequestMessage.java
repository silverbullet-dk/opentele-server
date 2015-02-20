
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="PersonCivilRegistrationIdentifier" type="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifierType"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}SelfMonitoredSample" maxOccurs="unbounded"/>
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
    "personCivilRegistrationIdentifier",
    "selfMonitoredSample"
})
@XmlRootElement(name = "CreateMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class CreateMonitoringDatasetRequestMessage {

    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected String personCivilRegistrationIdentifier;
    @XmlElement(name = "SelfMonitoredSample", namespace = "urn:oio:medcom:chronicdataset:1.0.1", required = true)
    protected List<SelfMonitoredSampleType> selfMonitoredSample;

    /**
     * Gets the value of the personCivilRegistrationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonCivilRegistrationIdentifier() {
        return personCivilRegistrationIdentifier;
    }

    /**
     * Sets the value of the personCivilRegistrationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonCivilRegistrationIdentifier(String value) {
        this.personCivilRegistrationIdentifier = value;
    }

    /**
     * Gets the value of the selfMonitoredSample property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the selfMonitoredSample property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSelfMonitoredSample().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SelfMonitoredSampleType }
     * 
     * 
     */
    public List<SelfMonitoredSampleType> getSelfMonitoredSample() {
        if (selfMonitoredSample == null) {
            selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>();
        }
        return this.selfMonitoredSample;
    }

}
