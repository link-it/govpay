
package it.gov.digitpa.schemas._2011.ws.psp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per paaAttivaRPT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="paaAttivaRPT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoPSP" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="datiPagamentoPSP" type="{http://ws.pagamenti.telematici.gov/}paaTipoDatiPagamentoPSP"/&gt;
 *         &lt;element name="identificativoIntermediarioPSP" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="identificativoCanalePSP" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paaAttivaRPT", propOrder = {
    "identificativoPSP",
    "datiPagamentoPSP",
    "identificativoIntermediarioPSP",
    "identificativoCanalePSP"
})
public class PaaAttivaRPT {

    @XmlElement(required = true)
    protected String identificativoPSP;
    @XmlElement(required = true)
    protected PaaTipoDatiPagamentoPSP datiPagamentoPSP;
    protected String identificativoIntermediarioPSP;
    protected String identificativoCanalePSP;

    /**
     * Recupera il valore della proprietà identificativoPSP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoPSP() {
        return identificativoPSP;
    }

    /**
     * Imposta il valore della proprietà identificativoPSP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoPSP(String value) {
        this.identificativoPSP = value;
    }

    /**
     * Recupera il valore della proprietà datiPagamentoPSP.
     * 
     * @return
     *     possible object is
     *     {@link PaaTipoDatiPagamentoPSP }
     *     
     */
    public PaaTipoDatiPagamentoPSP getDatiPagamentoPSP() {
        return datiPagamentoPSP;
    }

    /**
     * Imposta il valore della proprietà datiPagamentoPSP.
     * 
     * @param value
     *     allowed object is
     *     {@link PaaTipoDatiPagamentoPSP }
     *     
     */
    public void setDatiPagamentoPSP(PaaTipoDatiPagamentoPSP value) {
        this.datiPagamentoPSP = value;
    }

    /**
     * Recupera il valore della proprietà identificativoIntermediarioPSP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoIntermediarioPSP() {
        return identificativoIntermediarioPSP;
    }

    /**
     * Imposta il valore della proprietà identificativoIntermediarioPSP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoIntermediarioPSP(String value) {
        this.identificativoIntermediarioPSP = value;
    }

    /**
     * Recupera il valore della proprietà identificativoCanalePSP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoCanalePSP() {
        return identificativoCanalePSP;
    }

    /**
     * Imposta il valore della proprietà identificativoCanalePSP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoCanalePSP(String value) {
        this.identificativoCanalePSP = value;
    }

}
