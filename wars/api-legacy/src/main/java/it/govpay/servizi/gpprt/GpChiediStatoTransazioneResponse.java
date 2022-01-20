
package it.govpay.servizi.gpprt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.Transazione;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}gpResponse"&gt;
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
     * Gets the value of the transazione property.
     * 
     * @return
     *     possible object is
     *     {@link Transazione }
     *     
     */
    public Transazione getTransazione() {
        return transazione;
    }

    /**
     * Sets the value of the transazione property.
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
