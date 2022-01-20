
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the codEsito property.
     * 
     * @return
     *     possible object is
     *     {@link EsitoVerificaVersamento }
     *     
     */
    public EsitoVerificaVersamento getCodEsito() {
        return codEsito;
    }

    /**
     * Sets the value of the codEsito property.
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
     * Gets the value of the descrizioneEsito property.
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
     * Sets the value of the descrizioneEsito property.
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
     * Gets the value of the versamento property.
     * 
     * @return
     *     possible object is
     *     {@link Versamento }
     *     
     */
    public Versamento getVersamento() {
        return versamento;
    }

    /**
     * Sets the value of the versamento property.
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
