
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HealthAndPreventionServiceStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HealthAndPreventionServiceStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="offered"/>
 *     &lt;enumeration value="finish"/>
 *     &lt;enumeration value="not_relevant"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HealthAndPreventionServiceStatusType")
@XmlEnum
public enum HealthAndPreventionServiceStatusType {

    @XmlEnumValue("offered")
    OFFERED("offered"),
    @XmlEnumValue("finish")
    FINISH("finish"),
    @XmlEnumValue("not_relevant")
    NOT_RELEVANT("not_relevant");
    private final String value;

    HealthAndPreventionServiceStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HealthAndPreventionServiceStatusType fromValue(String v) {
        for (HealthAndPreventionServiceStatusType c: HealthAndPreventionServiceStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
