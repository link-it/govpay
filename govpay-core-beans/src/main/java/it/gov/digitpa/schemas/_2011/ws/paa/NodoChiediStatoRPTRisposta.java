
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediStatoRPTRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediStatoRPTRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="esito" type="{http://ws.pagamenti.telematici.gov/}esitoChiediStatoRPT" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediStatoRPTRisposta", propOrder = {
    "esito"
})
public class NodoChiediStatoRPTRisposta
    extends Risposta
{

    protected EsitoChiediStatoRPT esito;

    /**
     * Recupera il valore della proprietà esito.
     * 
     * @return
     *     possible object is
     *     {@link EsitoChiediStatoRPT }
     *     
     */
    public EsitoChiediStatoRPT getEsito() {
        return esito;
    }

    /**
     * Imposta il valore della proprietà esito.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoChiediStatoRPT }
     *     
     */
    public void setEsito(EsitoChiediStatoRPT value) {
        this.esito = value;
    }

}
