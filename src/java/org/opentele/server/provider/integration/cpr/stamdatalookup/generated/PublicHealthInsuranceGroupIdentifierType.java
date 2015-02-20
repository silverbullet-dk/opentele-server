
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PublicHealthInsuranceGroupIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PublicHealthInsuranceGroupIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="sygesikringsgruppe_1"/>
 *     &lt;enumeration value="sygesikringsgruppe_2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PublicHealthInsuranceGroupIdentifierType", namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/")
@XmlEnum
public enum PublicHealthInsuranceGroupIdentifierType {


    /**
     * Gruppe 1 daekning.
     * 
     */
    @XmlEnumValue("sygesikringsgruppe_1")
    SYGESIKRINGSGRUPPE_1("sygesikringsgruppe_1"),

    /**
     * Gruppe 2 daekning.
     * 
     */
    @XmlEnumValue("sygesikringsgruppe_2")
    SYGESIKRINGSGRUPPE_2("sygesikringsgruppe_2");
    private final String value;

    PublicHealthInsuranceGroupIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PublicHealthInsuranceGroupIdentifierType fromValue(String v) {
        for (PublicHealthInsuranceGroupIdentifierType c: PublicHealthInsuranceGroupIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
