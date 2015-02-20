
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AbnormalIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AbnormalIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="to_high"/>
 *     &lt;enumeration value="to_low"/>
 *     &lt;enumeration value="abnormal"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AbnormalIdentifierType")
@XmlEnum
public enum AbnormalIdentifierType {

    @XmlEnumValue("to_high")
    TO_HIGH("to_high"),
    @XmlEnumValue("to_low")
    TO_LOW("to_low"),
    @XmlEnumValue("abnormal")
    ABNORMAL("abnormal"),
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    AbnormalIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AbnormalIdentifierType fromValue(String v) {
        for (AbnormalIdentifierType c: AbnormalIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
