
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EncodingIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EncodingIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="numeric"/>
 *     &lt;enumeration value="alphanumeric"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EncodingIdentifierType")
@XmlEnum
public enum EncodingIdentifierType {

    @XmlEnumValue("numeric")
    NUMERIC("numeric"),
    @XmlEnumValue("alphanumeric")
    ALPHANUMERIC("alphanumeric");
    private final String value;

    EncodingIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EncodingIdentifierType fromValue(String v) {
        for (EncodingIdentifierType c: EncodingIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
