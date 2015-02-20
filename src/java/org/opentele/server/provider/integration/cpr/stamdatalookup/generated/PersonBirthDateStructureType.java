
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PersonBirthDateStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonBirthDateStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/}BirthDate"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/}BirthDateUncertaintyIndicator"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonBirthDateStructureType", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", propOrder = {
    "birthDate",
    "birthDateUncertaintyIndicator"
})
public class PersonBirthDateStructureType {

    @XmlElement(name = "BirthDate", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar birthDate;
    @XmlElement(name = "BirthDateUncertaintyIndicator")
    protected boolean birthDateUncertaintyIndicator;

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the birthDateUncertaintyIndicator property.
     * 
     */
    public boolean isBirthDateUncertaintyIndicator() {
        return birthDateUncertaintyIndicator;
    }

    /**
     * Sets the value of the birthDateUncertaintyIndicator property.
     * 
     */
    public void setBirthDateUncertaintyIndicator(boolean value) {
        this.birthDateUncertaintyIndicator = value;
    }

}
