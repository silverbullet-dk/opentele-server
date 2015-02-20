
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


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
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}SecurityLevel" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}TimeOut" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}Linking"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}FlowStatus" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}Priority" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}RequireNonRepudiationReceipt" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "securityLevel",
    "timeOut",
    "linking",
    "flowStatus",
    "priority",
    "requireNonRepudiationReceipt"
})
@XmlRootElement(name = "Header", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
public class Header {

    @XmlElement(name = "SecurityLevel", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected Integer securityLevel;
    @XmlElement(name = "TimeOut", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String timeOut;
    @XmlElement(name = "Linking", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", required = true)
    protected Linking linking;
    @XmlElement(name = "FlowStatus", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String flowStatus;
    @XmlElement(name = "Priority", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String priority;
    @XmlElement(name = "RequireNonRepudiationReceipt", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String requireNonRepudiationReceipt;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the securityLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecurityLevel() {
        return securityLevel;
    }

    /**
     * Sets the value of the securityLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecurityLevel(Integer value) {
        this.securityLevel = value;
    }

    /**
     * Gets the value of the timeOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * Sets the value of the timeOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeOut(String value) {
        this.timeOut = value;
    }

    /**
     * Gets the value of the linking property.
     * 
     * @return
     *     possible object is
     *     {@link Linking }
     *     
     */
    public Linking getLinking() {
        return linking;
    }

    /**
     * Sets the value of the linking property.
     * 
     * @param value
     *     allowed object is
     *     {@link Linking }
     *     
     */
    public void setLinking(Linking value) {
        this.linking = value;
    }

    /**
     * Gets the value of the flowStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowStatus() {
        return flowStatus;
    }

    /**
     * Sets the value of the flowStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowStatus(String value) {
        this.flowStatus = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the requireNonRepudiationReceipt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequireNonRepudiationReceipt() {
        return requireNonRepudiationReceipt;
    }

    /**
     * Sets the value of the requireNonRepudiationReceipt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequireNonRepudiationReceipt(String value) {
        this.requireNonRepudiationReceipt = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
