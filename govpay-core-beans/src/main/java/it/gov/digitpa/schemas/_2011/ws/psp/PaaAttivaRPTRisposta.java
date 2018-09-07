
package it.gov.digitpa.schemas._2011.ws.psp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per paaAttivaRPTRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="paaAttivaRPTRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="paaAttivaRPTRisposta" type="{http://ws.pagamenti.telematici.gov/}esitoAttivaRPT"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paaAttivaRPTRisposta", propOrder = {
    "paaAttivaRPTRisposta"
})
public class PaaAttivaRPTRisposta {

    @XmlElement(required = true)
    protected EsitoAttivaRPT paaAttivaRPTRisposta;

    /**
     * Recupera il valore della proprietà paaAttivaRPTRisposta.
     * 
     * @return
     *     possible object is
     *     {@link EsitoAttivaRPT }
     *     
     */
    public EsitoAttivaRPT getPaaAttivaRPTRisposta() {
        return this.paaAttivaRPTRisposta;
    }

    /**
     * Imposta il valore della proprietà paaAttivaRPTRisposta.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoAttivaRPT }
     *     
     */
    public void setPaaAttivaRPTRisposta(EsitoAttivaRPT value) {
        this.paaAttivaRPTRisposta = value;
    }

}
