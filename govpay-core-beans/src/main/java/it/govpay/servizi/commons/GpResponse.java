
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="codEsitoOperazione" type="{http://www.govpay.it/servizi/commons/}esitoOperazione"/&gt;
 *         &lt;element name="descrizioneEsitoOperazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "codEsitoOperazione",
    "descrizioneEsitoOperazione"
})
public class GpResponse {

    @XmlElement(required = true)
    protected String codOperazione;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EsitoOperazione codEsitoOperazione;
    protected String descrizioneEsitoOperazione;

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
     * Recupera il valore della proprietà codEsitoOperazione.
     * 
     * @return
     *     possible object is
     *     {@link EsitoOperazione }
     *     
     */
    public EsitoOperazione getCodEsitoOperazione() {
        return codEsitoOperazione;
    }

    /**
     * Imposta il valore della proprietà codEsitoOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoOperazione }
     *     
     */
    public void setCodEsitoOperazione(EsitoOperazione value) {
        this.codEsitoOperazione = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneEsitoOperazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneEsitoOperazione() {
        return descrizioneEsitoOperazione;
    }

    /**
     * Imposta il valore della proprietà descrizioneEsitoOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneEsitoOperazione(String value) {
        this.descrizioneEsitoOperazione = value;
    }

}
