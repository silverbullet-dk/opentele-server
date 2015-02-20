
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PersonLookupRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonLookupRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="NamePersonQuery" type="{http://nsi.dk/2011/09/23/StamdataCpr/}NamePersonQueryType"/>
 *         &lt;element name="BirthDatePersonQuery" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CivilRegistrationNumberPersonQuery" type="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifierType"/>
 *         &lt;element name="CivilRegistrationNumberListPersonQuery" type="{http://nsi.dk/2011/09/23/StamdataCpr/}CivilRegistrationNumberListPersonQueryType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonLookupRequestType", namespace = "http://nsi.dk/2011/09/23/StamdataCpr/", propOrder = {
    "namePersonQuery",
    "birthDatePersonQuery",
    "civilRegistrationNumberPersonQuery",
    "civilRegistrationNumberListPersonQuery"
})
@XmlRootElement(name = "PersonLookupRequest", namespace = "http://nsi.dk/2011/09/23/StamdataCpr/")
public class PersonLookupRequestType {

    @XmlElement(name = "NamePersonQuery", namespace = "")
    protected NamePersonQueryType namePersonQuery;
    @XmlElement(name = "BirthDatePersonQuery", namespace = "")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar birthDatePersonQuery;
    @XmlElement(name = "CivilRegistrationNumberPersonQuery", namespace = "")
    protected String civilRegistrationNumberPersonQuery;
    @XmlElement(name = "CivilRegistrationNumberListPersonQuery", namespace = "")
    protected CivilRegistrationNumberListPersonQueryType civilRegistrationNumberListPersonQuery;

    /**
     * Gets the value of the namePersonQuery property.
     * 
     * @return
     *     possible object is
     *     {@link NamePersonQueryType }
     *     
     */
    public NamePersonQueryType getNamePersonQuery() {
        return namePersonQuery;
    }

    /**
     * Sets the value of the namePersonQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamePersonQueryType }
     *     
     */
    public void setNamePersonQuery(NamePersonQueryType value) {
        this.namePersonQuery = value;
    }

    /**
     * Gets the value of the birthDatePersonQuery property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDatePersonQuery() {
        return birthDatePersonQuery;
    }

    /**
     * Sets the value of the birthDatePersonQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDatePersonQuery(XMLGregorianCalendar value) {
        this.birthDatePersonQuery = value;
    }

    /**
     * Gets the value of the civilRegistrationNumberPersonQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivilRegistrationNumberPersonQuery() {
        return civilRegistrationNumberPersonQuery;
    }

    /**
     * Sets the value of the civilRegistrationNumberPersonQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivilRegistrationNumberPersonQuery(String value) {
        this.civilRegistrationNumberPersonQuery = value;
    }

    /**
     * Gets the value of the civilRegistrationNumberListPersonQuery property.
     * 
     * @return
     *     possible object is
     *     {@link CivilRegistrationNumberListPersonQueryType }
     *     
     */
    public CivilRegistrationNumberListPersonQueryType getCivilRegistrationNumberListPersonQuery() {
        return civilRegistrationNumberListPersonQuery;
    }

    /**
     * Sets the value of the civilRegistrationNumberListPersonQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link CivilRegistrationNumberListPersonQueryType }
     *     
     */
    public void setCivilRegistrationNumberListPersonQuery(CivilRegistrationNumberListPersonQueryType value) {
        this.civilRegistrationNumberListPersonQuery = value;
    }

}
