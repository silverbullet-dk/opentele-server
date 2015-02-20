
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for _CountryIdentificationSchemeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="_CountryIdentificationSchemeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="iso3166-alpha2"/>
 *     &lt;enumeration value="iso3166-alpha3"/>
 *     &lt;enumeration value="un-numeric3"/>
 *     &lt;enumeration value="imk"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "_CountryIdentificationSchemeType", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/")
@XmlEnum
public enum CountryIdentificationSchemeType {


    /**
     * Dette format følge ISO 3166 standarden, alpha 2.
     * 
     */
    @XmlEnumValue("iso3166-alpha2")
    ISO_3166_ALPHA_2("iso3166-alpha2"),

    /**
     * Dette format følge ISO 3166 standarden, alpha 3.
     * 
     */
    @XmlEnumValue("iso3166-alpha3")
    ISO_3166_ALPHA_3("iso3166-alpha3"),

    /**
     * Dette format følger FNs Statistik Kontor landekoder
     * 
     */
    @XmlEnumValue("un-numeric3")
    UN_NUMERIC_3("un-numeric3"),

    /**
     * Dette format følger MyndighedsKoden fra Det Centrale Personregister
     * 
     */
    @XmlEnumValue("imk")
    IMK("imk");
    private final String value;

    CountryIdentificationSchemeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CountryIdentificationSchemeType fromValue(String v) {
        for (CountryIdentificationSchemeType c: CountryIdentificationSchemeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
