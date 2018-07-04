
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per canale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="canale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
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
    "codPsp",
    "codCanale",
    "tipoVersamento"
})
public class Canale {

    @XmlElement(required = true)
    protected String codPsp;
    @XmlElement(required = true)
    protected String codCanale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TipoVersamento tipoVersamento;

    /**
     * Recupera il valore della proprietà codPsp.
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
     * Imposta il valore della proprietà codPsp.
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
     * Recupera il valore della proprietà codCanale.
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
     * Imposta il valore della proprietà codCanale.
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
     * Recupera il valore della proprietà tipoVersamento.
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
     * Imposta il valore della proprietà tipoVersamento.
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
