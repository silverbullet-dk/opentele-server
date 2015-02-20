
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KramPredictorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KramPredictorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Weight" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Height" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Smoking" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Alcohol" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Exercise" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KramPredictorType", propOrder = {
    "weight",
    "height",
    "smoking",
    "alcohol",
    "exercise"
})
public class KramPredictorType {

    @XmlElement(name = "Weight")
    protected LaboratoryReportType weight;
    @XmlElement(name = "Height")
    protected LaboratoryReportType height;
    @XmlElement(name = "Smoking")
    protected LaboratoryReportType smoking;
    @XmlElement(name = "Alcohol")
    protected LaboratoryReportType alcohol;
    @XmlElement(name = "Exercise")
    protected LaboratoryReportType exercise;

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportType }
     *     
     */
    public LaboratoryReportType getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportType }
     *     
     */
    public void setWeight(LaboratoryReportType value) {
        this.weight = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportType }
     *     
     */
    public LaboratoryReportType getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportType }
     *     
     */
    public void setHeight(LaboratoryReportType value) {
        this.height = value;
    }

    /**
     * Gets the value of the smoking property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportType }
     *     
     */
    public LaboratoryReportType getSmoking() {
        return smoking;
    }

    /**
     * Sets the value of the smoking property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportType }
     *     
     */
    public void setSmoking(LaboratoryReportType value) {
        this.smoking = value;
    }

    /**
     * Gets the value of the alcohol property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportType }
     *     
     */
    public LaboratoryReportType getAlcohol() {
        return alcohol;
    }

    /**
     * Sets the value of the alcohol property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportType }
     *     
     */
    public void setAlcohol(LaboratoryReportType value) {
        this.alcohol = value;
    }

    /**
     * Gets the value of the exercise property.
     * 
     * @return
     *     possible object is
     *     {@link LaboratoryReportType }
     *     
     */
    public LaboratoryReportType getExercise() {
        return exercise;
    }

    /**
     * Sets the value of the exercise property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaboratoryReportType }
     *     
     */
    public void setExercise(LaboratoryReportType value) {
        this.exercise = value;
    }

}
