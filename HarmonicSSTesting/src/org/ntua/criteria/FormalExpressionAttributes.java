//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.13 at 01:56:31 PM EET 
//


package org.ntua.criteria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				This type defines the attributes a Formal Expression should have.
 * 			
 * 
 * <p>Java class for formalExpressionAttributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="formalExpressionAttributes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Language" use="required" type="{http://www.ntua.org/criteria}formalExpressionLanguage" />
 *       &lt;attribute name="Model" use="required" type="{http://www.ntua.org/criteria}formalExpressionContent" />
 *       &lt;attribute name="ProducedBy" use="required" type="{http://www.ntua.org/criteria}module" />
 *       &lt;attribute name="Origin" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="InfoLoss" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formalExpressionAttributes")
@XmlSeeAlso({
    FormalExpression.class
})
public class FormalExpressionAttributes {

    @XmlAttribute(name = "ID", required = true)
    protected String id;
    @XmlAttribute(name = "Language", required = true)
    protected FormalExpressionLanguage language;
    @XmlAttribute(name = "Model", required = true)
    protected FormalExpressionContent model;
    @XmlAttribute(name = "ProducedBy", required = true)
    protected Module producedBy;
    @XmlAttribute(name = "Origin")
    protected String origin;
    @XmlAttribute(name = "InfoLoss")
    protected Boolean infoLoss;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link FormalExpressionLanguage }
     *     
     */
    public FormalExpressionLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormalExpressionLanguage }
     *     
     */
    public void setLanguage(FormalExpressionLanguage value) {
        this.language = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link FormalExpressionContent }
     *     
     */
    public FormalExpressionContent getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormalExpressionContent }
     *     
     */
    public void setModel(FormalExpressionContent value) {
        this.model = value;
    }

    /**
     * Gets the value of the producedBy property.
     * 
     * @return
     *     possible object is
     *     {@link Module }
     *     
     */
    public Module getProducedBy() {
        return producedBy;
    }

    /**
     * Sets the value of the producedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Module }
     *     
     */
    public void setProducedBy(Module value) {
        this.producedBy = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

    /**
     * Gets the value of the infoLoss property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInfoLoss() {
        return infoLoss;
    }

    /**
     * Sets the value of the infoLoss property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInfoLoss(Boolean value) {
        this.infoLoss = value;
    }

}