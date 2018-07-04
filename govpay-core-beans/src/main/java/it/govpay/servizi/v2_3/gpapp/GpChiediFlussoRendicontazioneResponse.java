
package it.govpay.servizi.v2_3.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione;
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
 *         &lt;element name="flussoRendicontazione" type="{http://www.govpay.it/servizi/v2_3/commons/}flussoRendicontazione"/&gt;
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
    "flussoRendicontazione"
})
@XmlRootElement(name = "gpChiediFlussoRendicontazioneResponse")
public class GpChiediFlussoRendicontazioneResponse
    extends GpResponse
{

    protected FlussoRendicontazione flussoRendicontazione;

    /**
     * Recupera il valore della proprietà flussoRendicontazione.
     * 
     * @return
     *     possible object is
     *     {@link FlussoRendicontazione }
     *     
     */
    public FlussoRendicontazione getFlussoRendicontazione() {
        return flussoRendicontazione;
    }

    /**
     * Imposta il valore della proprietà flussoRendicontazione.
     * 
     * @param value
     *     allowed object is
     *     {@link FlussoRendicontazione }
     *     
     */
    public void setFlussoRendicontazione(FlussoRendicontazione value) {
        this.flussoRendicontazione = value;
    }

}
