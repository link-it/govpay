
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gpResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the codOperazione property.
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
     * Sets the value of the codOperazione property.
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
     * Gets the value of the codEsitoOperazione property.
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
     * Sets the value of the codEsitoOperazione property.
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
     * Gets the value of the descrizioneEsitoOperazione property.
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
     * Sets the value of the descrizioneEsitoOperazione property.
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
