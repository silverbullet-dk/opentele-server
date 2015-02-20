
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

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
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}FlowID" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}MessageID"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}InResponseToMessageID" minOccurs="0"/>
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
    "flowID",
    "messageID",
    "inResponseToMessageID"
})
@XmlRootElement(name = "Linking", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
public class Linking {

    @XmlElement(name = "FlowID", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String flowID;
    @XmlElement(name = "MessageID", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", required = true)
    protected String messageID;
    @XmlElement(name = "InResponseToMessageID", namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd")
    protected String inResponseToMessageID;

    /**
     * Gets the value of the flowID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowID() {
        return flowID;
    }

    /**
     * Sets the value of the flowID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowID(String value) {
        this.flowID = value;
    }

    /**
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the inResponseToMessageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInResponseToMessageID() {
        return inResponseToMessageID;
    }

    /**
     * Sets the value of the inResponseToMessageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInResponseToMessageID(String value) {
        this.inResponseToMessageID = value;
    }

}
