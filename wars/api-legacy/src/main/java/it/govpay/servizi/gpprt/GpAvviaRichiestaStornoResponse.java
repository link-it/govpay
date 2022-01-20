
package it.govpay.servizi.gpprt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}gpResponse"&gt;
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
     * Gets the value of the codRichiestaStorno property.
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
     * Sets the value of the codRichiestaStorno property.
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
