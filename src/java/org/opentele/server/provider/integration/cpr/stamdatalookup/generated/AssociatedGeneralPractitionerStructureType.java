
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Informationer om den praktiserende laege der er tildelt en person.
 * 
 * <p>Java class for AssociatedGeneralPractitionerStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssociatedGeneralPractitionerStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}AssociatedGeneralPractitionerIdentifier"/>
 *         &lt;element ref="{http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/}AssociatedGeneralPractitionerOrganisationName"/>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/}StandardAddressIdentifier"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/}PostCodeIdentifier"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/}DistrictName"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/09/01/}TelephoneSubscriberIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/}EmailAddressIdentifier" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssociatedGeneralPractitionerStructureType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "associatedGeneralPractitionerIdentifier",
    "associatedGeneralPractitionerOrganisationName",
    "standardAddressIdentifier",
    "postCodeIdentifier",
    "districtName",
    "telephoneSubscriberIdentifier",
    "emailAddressIdentifier"
})
public class AssociatedGeneralPractitionerStructureType {

    @XmlElement(name = "AssociatedGeneralPractitionerIdentifier", required = true)
    protected BigInteger associatedGeneralPractitionerIdentifier;
    @XmlElement(name = "AssociatedGeneralPractitionerOrganisationName", required = true)
    protected String associatedGeneralPractitionerOrganisationName;
    @XmlElement(name = "StandardAddressIdentifier", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/", required = true)
    protected String standardAddressIdentifier;
    @XmlElement(name = "PostCodeIdentifier", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", required = true)
    protected String postCodeIdentifier;
    @XmlElement(name = "DistrictName", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", required = true)
    protected String districtName;
    @XmlElement(name = "TelephoneSubscriberIdentifier", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/09/01/")
    protected String telephoneSubscriberIdentifier;
    @XmlElement(name = "EmailAddressIdentifier", namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/")
    protected String emailAddressIdentifier;

    /**
     * Ydernummeret paa den praktiserende laege
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAssociatedGeneralPractitionerIdentifier() {
        return associatedGeneralPractitionerIdentifier;
    }

    /**
     * Sets the value of the associatedGeneralPractitionerIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAssociatedGeneralPractitionerIdentifier(BigInteger value) {
        this.associatedGeneralPractitionerIdentifier = value;
    }

    /**
     * Navn paa laegepraksis eller den praktiserende laege.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssociatedGeneralPractitionerOrganisationName() {
        return associatedGeneralPractitionerOrganisationName;
    }

    /**
     * Sets the value of the associatedGeneralPractitionerOrganisationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssociatedGeneralPractitionerOrganisationName(String value) {
        this.associatedGeneralPractitionerOrganisationName = value;
    }

    /**
     * Postadresse paa laegepraksis
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardAddressIdentifier() {
        return standardAddressIdentifier;
    }

    /**
     * Sets the value of the standardAddressIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardAddressIdentifier(String value) {
        this.standardAddressIdentifier = value;
    }

    /**
     * Postnummeret paa laegepraksis
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostCodeIdentifier() {
        return postCodeIdentifier;
    }

    /**
     * Sets the value of the postCodeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostCodeIdentifier(String value) {
        this.postCodeIdentifier = value;
    }

    /**
     * Bynavn for laegepraksis
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistrictName() {
        return districtName;
    }

    /**
     * Sets the value of the districtName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistrictName(String value) {
        this.districtName = value;
    }

    /**
     * Telefonnummer paa laege/praksis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneSubscriberIdentifier() {
        return telephoneSubscriberIdentifier;
    }

    /**
     * Sets the value of the telephoneSubscriberIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneSubscriberIdentifier(String value) {
        this.telephoneSubscriberIdentifier = value;
    }

    /**
     * Email-adresse paa laege/praksis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddressIdentifier() {
        return emailAddressIdentifier;
    }

    /**
     * Sets the value of the emailAddressIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddressIdentifier(String value) {
        this.emailAddressIdentifier = value;
    }

}
