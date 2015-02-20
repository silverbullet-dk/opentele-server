
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperatorIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperatorIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="less_than"/>
 *     &lt;enumeration value="greater_than"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OperatorIdentifierType")
@XmlEnum
public enum OperatorIdentifierType {

    @XmlEnumValue("less_than")
    LESS_THAN("less_than"),
    @XmlEnumValue("greater_than")
    GREATER_THAN("greater_than");
    private final String value;

    OperatorIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperatorIdentifierType fromValue(String v) {
        for (OperatorIdentifierType c: OperatorIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
