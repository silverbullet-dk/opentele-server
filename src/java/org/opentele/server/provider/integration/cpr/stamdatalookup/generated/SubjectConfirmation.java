
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
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}ConfirmationMethod"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmationData"/>
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
    "confirmationMethod",
    "subjectConfirmationData"
})
@XmlRootElement(name = "SubjectConfirmation", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
public class SubjectConfirmation {

    @XmlElement(name = "ConfirmationMethod", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", required = true)
    protected String confirmationMethod;
    @XmlElement(name = "SubjectConfirmationData", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", required = true)
    protected SubjectConfirmationData subjectConfirmationData;

    /**
     * Gets the value of the confirmationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmationMethod() {
        return confirmationMethod;
    }

    /**
     * Sets the value of the confirmationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmationMethod(String value) {
        this.confirmationMethod = value;
    }

    /**
     * Gets the value of the subjectConfirmationData property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmationData }
     *     
     */
    public SubjectConfirmationData getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /**
     * Sets the value of the subjectConfirmationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmationData }
     *     
     */
    public void setSubjectConfirmationData(SubjectConfirmationData value) {
        this.subjectConfirmationData = value;
    }

}
