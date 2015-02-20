
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PersonCivilRegistrationStatusStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonCivilRegistrationStatusStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/}PersonCivilRegistrationStatusCode"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/}PersonCivilRegistrationStatusStartDate"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonCivilRegistrationStatusStructureType", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", propOrder = {
    "personCivilRegistrationStatusCode",
    "personCivilRegistrationStatusStartDate"
})
public class PersonCivilRegistrationStatusStructureType {

    @XmlElement(name = "PersonCivilRegistrationStatusCode", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", required = true)
    protected BigInteger personCivilRegistrationStatusCode;
    @XmlElement(name = "PersonCivilRegistrationStatusStartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar personCivilRegistrationStatusStartDate;

    /**
     * Gets the value of the personCivilRegistrationStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPersonCivilRegistrationStatusCode() {
        return personCivilRegistrationStatusCode;
    }

    /**
     * Sets the value of the personCivilRegistrationStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPersonCivilRegistrationStatusCode(BigInteger value) {
        this.personCivilRegistrationStatusCode = value;
    }

    /**
     * Gets the value of the personCivilRegistrationStatusStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonCivilRegistrationStatusStartDate() {
        return personCivilRegistrationStatusStartDate;
    }

    /**
     * Sets the value of the personCivilRegistrationStatusStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonCivilRegistrationStatusStartDate(XMLGregorianCalendar value) {
        this.personCivilRegistrationStatusStartDate = value;
    }

}
