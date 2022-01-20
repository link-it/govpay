
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for canale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="canale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codIntermediarioPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codCanale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="tipoVersamento" type="{http://www.govpay.it/servizi/commons/}tipoVersamento"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "canale", propOrder = {
    "codIntermediarioPsp",
    "codPsp",
    "codCanale",
    "tipoVersamento"
})
public class Canale {

    @XmlElement(required = true)
    protected String codIntermediarioPsp;
    @XmlElement(required = true)
    protected String codPsp;
    @XmlElement(required = true)
    protected String codCanale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TipoVersamento tipoVersamento;

    /**
     * Gets the value of the codIntermediarioPsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodIntermediarioPsp() {
        return codIntermediarioPsp;
    }

    /**
     * Sets the value of the codIntermediarioPsp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodIntermediarioPsp(String value) {
        this.codIntermediarioPsp = value;
    }

    /**
     * Gets the value of the codPsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPsp() {
        return codPsp;
    }

    /**
     * Sets the value of the codPsp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPsp(String value) {
        this.codPsp = value;
    }

    /**
     * Gets the value of the codCanale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCanale() {
        return codCanale;
    }

    /**
     * Sets the value of the codCanale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCanale(String value) {
        this.codCanale = value;
    }

    /**
     * Gets the value of the tipoVersamento property.
     * 
     * @return
     *     possible object is
     *     {@link TipoVersamento }
     *     
     */
    public TipoVersamento getTipoVersamento() {
        return tipoVersamento;
    }

    /**
     * Sets the value of the tipoVersamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoVersamento }
     *     
     */
    public void setTipoVersamento(TipoVersamento value) {
        this.tipoVersamento = value;
    }

}
