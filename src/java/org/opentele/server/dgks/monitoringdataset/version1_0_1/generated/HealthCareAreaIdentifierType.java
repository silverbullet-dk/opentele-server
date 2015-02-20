
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HealthCareAreaIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HealthCareAreaIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="gp"/>
 *     &lt;enumeration value="county"/>
 *     &lt;enumeration value="hospital"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HealthCareAreaIdentifierType")
@XmlEnum
public enum HealthCareAreaIdentifierType {

    @XmlEnumValue("gp")
    GP("gp"),
    @XmlEnumValue("county")
    COUNTY("county"),
    @XmlEnumValue("hospital")
    HOSPITAL("hospital"),
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    HealthCareAreaIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HealthCareAreaIdentifierType fromValue(String v) {
        for (HealthCareAreaIdentifierType c: HealthCareAreaIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
