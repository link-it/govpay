
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediElencoFlussiRendicontazioneRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediElencoFlussiRendicontazioneRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elencoFlussiRendicontazione" type="{http://ws.pagamenti.telematici.gov/}tipoElencoFlussiRendicontazione" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediElencoFlussiRendicontazioneRisposta", propOrder = {
    "elencoFlussiRendicontazione"
})
public class NodoChiediElencoFlussiRendicontazioneRisposta
    extends Risposta
{

    protected TipoElencoFlussiRendicontazione elencoFlussiRendicontazione;

    /**
     * Recupera il valore della proprietà elencoFlussiRendicontazione.
     * 
     * @return
     *     possible object is
     *     {@link TipoElencoFlussiRendicontazione }
     *     
     */
    public TipoElencoFlussiRendicontazione getElencoFlussiRendicontazione() {
        return elencoFlussiRendicontazione;
    }

    /**
     * Imposta il valore della proprietà elencoFlussiRendicontazione.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoElencoFlussiRendicontazione }
     *     
     */
    public void setElencoFlussiRendicontazione(TipoElencoFlussiRendicontazione value) {
        this.elencoFlussiRendicontazione = value;
    }

}
