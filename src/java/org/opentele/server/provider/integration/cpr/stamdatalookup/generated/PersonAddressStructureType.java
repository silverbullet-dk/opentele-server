
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Postaddresse paa en person i Danmark samt information vedr. adressebeskyttelse.
 *             
 * 
 * <p>Java class for PersonAddressStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonAddressStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/}CareOfName" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/}AddressComplete"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/09/01/}PersonInformationProtectionStartDate" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ois.dk/xml/schemas/2006/04/25/}CountyCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonAddressStructureType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", propOrder = {
    "careOfName",
    "addressComplete",
    "personInformationProtectionStartDate",
    "countyCode"
})
public class PersonAddressStructureType {

    @XmlElement(name = "CareOfName", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/")
    protected String careOfName;
    @XmlElement(name = "AddressComplete", namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", required = true)
    protected AddressCompleteType addressComplete;
    @XmlElement(name = "PersonInformationProtectionStartDate", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/09/01/")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar personInformationProtectionStartDate;
    @XmlElement(name = "CountyCode", namespace = "http://rep.oio.dk/ois.dk/xml/schemas/2006/04/25/", required = true)
    protected String countyCode;

    /**
     * Angiver om denne adresse er en C/O adresse
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareOfName() {
        return careOfName;
    }

    /**
     * Sets the value of the careOfName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareOfName(String value) {
        this.careOfName = value;
    }

    /**
     * Adresse i danmark
     * 
     * @return
     *     possible object is
     *     {@link AddressCompleteType }
     *     
     */
    public AddressCompleteType getAddressComplete() {
        return addressComplete;
    }

    /**
     * Sets the value of the addressComplete property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressCompleteType }
     *     
     */
    public void setAddressComplete(AddressCompleteType value) {
        this.addressComplete = value;
    }

    /**
     * Hvis dette felt forekommer er adressen beskyttet fra denne dato, ellers ingen
     *                         beskyttelse.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonInformationProtectionStartDate() {
        return personInformationProtectionStartDate;
    }

    /**
     * Sets the value of the personInformationProtectionStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonInformationProtectionStartDate(XMLGregorianCalendar value) {
        this.personInformationProtectionStartDate = value;
    }

    /**
     * Gets the value of the countyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountyCode() {
        return countyCode;
    }

    /**
     * Sets the value of the countyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountyCode(String value) {
        this.countyCode = value;
    }

}
