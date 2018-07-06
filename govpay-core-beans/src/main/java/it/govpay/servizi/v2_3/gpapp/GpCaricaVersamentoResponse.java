
package it.govpay.servizi.v2_3.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.IuvGenerato;
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
 *       &lt;sequence&gt;
 *         &lt;element name="iuvGenerato" type="{http://www.govpay.it/servizi/commons/}iuvGenerato" minOccurs="0"/&gt;
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
    "iuvGenerato"
})
@XmlRootElement(name = "gpCaricaVersamentoResponse")
public class GpCaricaVersamentoResponse
    extends GpResponse
{

    protected IuvGenerato iuvGenerato;

    /**
     * Recupera il valore della proprietà iuvGenerato.
     * 
     * @return
     *     possible object is
     *     {@link IuvGenerato }
     *     
     */
    public IuvGenerato getIuvGenerato() {
        return iuvGenerato;
    }

    /**
     * Imposta il valore della proprietà iuvGenerato.
     * 
     * @param value
     *     allowed object is
     *     {@link IuvGenerato }
     *     
     */
    public void setIuvGenerato(IuvGenerato value) {
        this.iuvGenerato = value;
    }

}
