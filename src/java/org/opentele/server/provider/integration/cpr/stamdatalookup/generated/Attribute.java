
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AttributeValue"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Name" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="sosi:IDCardID"/>
 *             &lt;enumeration value="sosi:IDCardVersion"/>
 *             &lt;enumeration value="sosi:IDCardType"/>
 *             &lt;enumeration value="sosi:AuthenticationLevel"/>
 *             &lt;enumeration value="sosi:OCESCertHash"/>
 *             &lt;enumeration value="medcom:UserCivilRegistrationNumber"/>
 *             &lt;enumeration value="medcom:UserGivenName"/>
 *             &lt;enumeration value="medcom:UserSurName"/>
 *             &lt;enumeration value="medcom:UserEmailAddress"/>
 *             &lt;enumeration value="medcom:UserRole"/>
 *             &lt;enumeration value="medcom:UserOccupation"/>
 *             &lt;enumeration value="medcom:UserAuthorizationCode"/>
 *             &lt;enumeration value="medcom:CareProviderID"/>
 *             &lt;enumeration value="medcom:CareProviderName"/>
 *             &lt;enumeration value="medcom:ITSystemName"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="NameFormat" type="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectIdentifierType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "attributeValue"
})
@XmlRootElement(name = "Attribute", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
public class Attribute {

    @XmlElement(name = "AttributeValue", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", required = true)
    protected String attributeValue;
    @XmlAttribute(name = "Name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "NameFormat")
    protected String nameFormat;

    /**
     * Gets the value of the attributeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Sets the value of the attributeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeValue(String value) {
        this.attributeValue = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nameFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameFormat() {
        return nameFormat;
    }

    /**
     * Sets the value of the nameFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameFormat(String value) {
        this.nameFormat = value;
    }

}
