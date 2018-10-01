
package it.govpay.servizi.v2_3.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codFlusso" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codApplicazione",
    "codFlusso"
})
@XmlRootElement(name = "gpChiediFlussoRendicontazione")
public class GpChiediFlussoRendicontazione {

    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true)
    protected String codFlusso;

    /**
     * Recupera il valore della proprietà codApplicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodApplicazione() {
        return this.codApplicazione;
    }

    /**
     * Imposta il valore della proprietà codApplicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodApplicazione(String value) {
        this.codApplicazione = value;
    }

    /**
     * Recupera il valore della proprietà codFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFlusso() {
        return this.codFlusso;
    }

    /**
     * Imposta il valore della proprietà codFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFlusso(String value) {
        this.codFlusso = value;
    }

}
