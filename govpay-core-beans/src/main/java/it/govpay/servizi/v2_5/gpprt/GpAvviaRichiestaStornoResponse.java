
package it.govpay.servizi.v2_5.gpprt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="codRichiestaStorno" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
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
    "codRichiestaStorno"
})
@XmlRootElement(name = "gpAvviaRichiestaStornoResponse")
public class GpAvviaRichiestaStornoResponse
    extends GpResponse
{

    protected String codRichiestaStorno;

    /**
     * Recupera il valore della proprietà codRichiestaStorno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRichiestaStorno() {
        return codRichiestaStorno;
    }

    /**
     * Imposta il valore della proprietà codRichiestaStorno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRichiestaStorno(String value) {
        this.codRichiestaStorno = value;
    }

}
