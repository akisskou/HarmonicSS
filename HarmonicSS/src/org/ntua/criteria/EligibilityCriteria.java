//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.13 at 01:56:31 PM EET 
//


package org.ntua.criteria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				The key standards that people who want to participate in a clinical study must meet or 
 * 				the characteristics that they must have in order to be eligible to participate in 
 * 				the clinical study. 
 * 				These include inclusion criteria and exclusion criteria.
 * 			
 * 
 * <p>Java class for eligibilityCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eligibilityCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InclusionCriteria" type="{http://www.ntua.org/criteria}criteriaList"/>
 *         &lt;element name="ExclusionCriteria" type="{http://www.ntua.org/criteria}criteriaList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eligibilityCriteria", propOrder = {
    "inclusionCriteria",
    "exclusionCriteria"
})
public class EligibilityCriteria {

    @XmlElement(name = "InclusionCriteria", required = true)
    protected CriteriaList inclusionCriteria;
    @XmlElement(name = "ExclusionCriteria", required = true)
    protected CriteriaList exclusionCriteria;

    /**
     * Gets the value of the inclusionCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaList }
     *     
     */
    public CriteriaList getInclusionCriteria() {
        return inclusionCriteria;
    }

    /**
     * Sets the value of the inclusionCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaList }
     *     
     */
    public void setInclusionCriteria(CriteriaList value) {
        this.inclusionCriteria = value;
    }

    /**
     * Gets the value of the exclusionCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaList }
     *     
     */
    public CriteriaList getExclusionCriteria() {
        return exclusionCriteria;
    }

    /**
     * Sets the value of the exclusionCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaList }
     *     
     */
    public void setExclusionCriteria(CriteriaList value) {
        this.exclusionCriteria = value;
    }

}