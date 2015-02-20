
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MeasuringDataClassificationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MeasuringDataClassificationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="clinical"/>
 *     &lt;enumeration value="notclinical"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MeasuringDataClassificationType", namespace = "urn:oio:medcom:chronicdataset:1.0.1")
@XmlEnum
public enum MeasuringDataClassificationType {

    @XmlEnumValue("clinical")
    CLINICAL("clinical"),
    @XmlEnumValue("notclinical")
    NOTCLINICAL("notclinical");
    private final String value;

    MeasuringDataClassificationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MeasuringDataClassificationType fromValue(String v) {
        for (MeasuringDataClassificationType c: MeasuringDataClassificationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
