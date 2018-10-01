
package it.govpay.servizi.pa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.EsitoVerificaVersamento;
import it.govpay.servizi.commons.Versamento;


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
 *         &lt;element name="codEsito" type="{http://www.govpay.it/servizi/commons/}esitoVerificaVersamento"/&gt;
 *         &lt;element name="descrizioneEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versamento" type="{http://www.govpay.it/servizi/commons/}versamento" minOccurs="0"/&gt;
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
    "codEsito",
    "descrizioneEsito",
    "versamento"
})
@XmlRootElement(name = "paVerificaVersamentoResponse")
public class PaVerificaVersamentoResponse {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EsitoVerificaVersamento codEsito;
    protected String descrizioneEsito;
    protected Versamento versamento;

    /**
     * Recupera il valore della proprietà codEsito.
     * 
     * @return
     *     possible object is
     *     {@link EsitoVerificaVersamento }
     *     
     */
    public EsitoVerificaVersamento getCodEsito() {
        return this.codEsito;
    }

    /**
     * Imposta il valore della proprietà codEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoVerificaVersamento }
     *     
     */
    public void setCodEsito(EsitoVerificaVersamento value) {
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
        return this.descrizioneEsito;
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
     * Recupera il valore della proprietà versamento.
     * 
     * @return
     *     possible object is
     *     {@link Versamento }
     *     
     */
    public Versamento getVersamento() {
        return this.versamento;
    }

    /**
     * Imposta il valore della proprietà versamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Versamento }
     *     
     */
    public void setVersamento(Versamento value) {
        this.versamento = value;
    }

}
