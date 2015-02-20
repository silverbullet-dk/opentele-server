
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MeasurementScheduledType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MeasurementScheduledType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="scheduled"/>
 *     &lt;enumeration value="notscheduled"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MeasurementScheduledType", namespace = "urn:oio:medcom:chronicdataset:1.0.1")
@XmlEnum
public enum MeasurementScheduledType {

    @XmlEnumValue("scheduled")
    SCHEDULED("scheduled"),
    @XmlEnumValue("notscheduled")
    NOTSCHEDULED("notscheduled");
    private final String value;

    MeasurementScheduledType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MeasurementScheduledType fromValue(String v) {
        for (MeasurementScheduledType c: MeasurementScheduledType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
