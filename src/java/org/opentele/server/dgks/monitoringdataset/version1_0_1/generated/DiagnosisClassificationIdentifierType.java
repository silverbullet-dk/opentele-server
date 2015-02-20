
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DiagnosisClassificationIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DiagnosisClassificationIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SKS"/>
 *     &lt;enumeration value="ICD10"/>
 *     &lt;enumeration value="ICPC"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DiagnosisClassificationIdentifierType")
@XmlEnum
public enum DiagnosisClassificationIdentifierType {

    SKS("SKS"),
    @XmlEnumValue("ICD10")
    ICD_10("ICD10"),
    ICPC("ICPC"),
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    DiagnosisClassificationIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DiagnosisClassificationIdentifierType fromValue(String v) {
        for (DiagnosisClassificationIdentifierType c: DiagnosisClassificationIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
