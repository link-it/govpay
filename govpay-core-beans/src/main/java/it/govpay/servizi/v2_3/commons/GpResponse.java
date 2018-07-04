
package it.govpay.servizi.v2_3.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.v2_3.gprnd.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.v2_3.gprnd.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.v2_3.gprnd.GpRegistraIncassoResponse;


/**
 * <p>Classe Java per gpResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="gpResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codOperazione" type="{http://www.govpay.it/servizi/commons/}uuid"/&gt;
 *         &lt;element name="mittente" type="{http://www.govpay.it/servizi/v2_3/commons/}mittente"/&gt;
 *         &lt;element name="codEsito" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="descrizioneEsito" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gpResponse", propOrder = {
    "codOperazione",
    "mittente",
    "codEsito",
    "descrizioneEsito",
    "dettaglioEsito"
})
@XmlSeeAlso({
    GpRegistraIncassoResponse.class,
    GpChiediFlussoRendicontazioneResponse.class,
    GpChiediListaFlussiRendicontazioneResponse.class
})
public class GpResponse {

    @XmlElement(required = true)
    protected String codOperazione;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Mittente mittente;
    @XmlElement(required = true)
    protected String codEsito;
    @XmlElement(required = true)
    protected String descrizioneEsito;
    protected String dettaglioEsito;

    /**
     * Recupera il valore della proprietà codOperazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOperazione() {
        return codOperazione;
    }

    /**
     * Imposta il valore della proprietà codOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOperazione(String value) {
        this.codOperazione = value;
    }

    /**
     * Recupera il valore della proprietà mittente.
     * 
     * @return
     *     possible object is
     *     {@link Mittente }
     *     
     */
    public Mittente getMittente() {
        return mittente;
    }

    /**
     * Imposta il valore della proprietà mittente.
     * 
     * @param value
     *     allowed object is
     *     {@link Mittente }
     *     
     */
    public void setMittente(Mittente value) {
        this.mittente = value;
    }

    /**
     * Recupera il valore della proprietà codEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEsito() {
        return codEsito;
    }

    /**
     * Imposta il valore della proprietà codEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEsito(String value) {
        this.codEsito = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneEsito() {
        return descrizioneEsito;
    }

    /**
     * Imposta il valore della proprietà descrizioneEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneEsito(String value) {
        this.descrizioneEsito = value;
    }

    /**
     * Recupera il valore della proprietà dettaglioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettaglioEsito() {
        return dettaglioEsito;
    }

    /**
     * Imposta il valore della proprietà dettaglioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettaglioEsito(String value) {
        this.dettaglioEsito = value;
    }

}
