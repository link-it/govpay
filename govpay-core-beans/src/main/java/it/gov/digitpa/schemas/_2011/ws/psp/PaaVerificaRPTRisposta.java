
package it.gov.digitpa.schemas._2011.ws.psp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per paaVerificaRPTRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="paaVerificaRPTRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="paaVerificaRPTRisposta" type="{http://ws.pagamenti.telematici.gov/}esitoVerificaRPT"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paaVerificaRPTRisposta", propOrder = {
    "paaVerificaRPTRisposta"
})
public class PaaVerificaRPTRisposta {

    @XmlElement(required = true)
    protected EsitoVerificaRPT paaVerificaRPTRisposta;

    /**
     * Recupera il valore della proprietà paaVerificaRPTRisposta.
     * 
     * @return
     *     possible object is
     *     {@link EsitoVerificaRPT }
     *     
     */
    public EsitoVerificaRPT getPaaVerificaRPTRisposta() {
        return this.paaVerificaRPTRisposta;
    }

    /**
     * Imposta il valore della proprietà paaVerificaRPTRisposta.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoVerificaRPT }
     *     
     */
    public void setPaaVerificaRPTRisposta(EsitoVerificaRPT value) {
        this.paaVerificaRPTRisposta = value;
    }

}
