
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
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmation" minOccurs="0"/>
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
    "nameID",
    "subjectConfirmation"
})
@XmlRootElement(name = "Subject", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
public class Subject {

    @XmlElement(name = "NameID", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", required = true)
    protected NameIDType nameID;
    @XmlElement(name = "SubjectConfirmation", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected SubjectConfirmation subjectConfirmation;

    /**
     * Gets the value of the nameID property.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the subjectConfirmation property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmation }
     *     
     */
    public SubjectConfirmation getSubjectConfirmation() {
        return subjectConfirmation;
    }

    /**
     * Sets the value of the subjectConfirmation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmation }
     *     
     */
    public void setSubjectConfirmation(SubjectConfirmation value) {
        this.subjectConfirmation = value;
    }

}
