
package it.govpay.servizi.v2_5.gpprt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.v2_3.commons.GpResponse;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="transazione" type="{http://www.govpay.it/servizi/commons/}transazione" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transazione"
})
@XmlRootElement(name = "gpChiediStatoTransazioneResponse")
public class GpChiediStatoTransazioneResponse
    extends GpResponse
{

    protected Transazione transazione;

    /**
     * Recupera il valore della proprietà transazione.
     * 
     * @return
     *     possible object is
     *     {@link Transazione }
     *     
     */
    public Transazione getTransazione() {
        return this.transazione;
    }

    /**
     * Imposta il valore della proprietà transazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Transazione }
     *     
     */
    public void setTransazione(Transazione value) {
        this.transazione = value;
    }

}
