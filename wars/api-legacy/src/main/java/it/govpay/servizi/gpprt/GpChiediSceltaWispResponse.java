
package it.govpay.servizi.gpprt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.TipoSceltaWisp;


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
 *         &lt;element name="scelta" type="{http://www.govpay.it/servizi/commons/}tipoSceltaWisp"/&gt;
 *         &lt;element name="canale" type="{http://www.govpay.it/servizi/commons/}canale" minOccurs="0"/&gt;
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
    "scelta",
    "canale"
})
@XmlRootElement(name = "gpChiediSceltaWispResponse")
public class GpChiediSceltaWispResponse
    extends GpResponse
{

    @XmlSchemaType(name = "string")
    protected TipoSceltaWisp scelta;
    protected Canale canale;

    /**
     * Gets the value of the scelta property.
     * 
     * @return
     *     possible object is
     *     {@link TipoSceltaWisp }
     *     
     */
    public TipoSceltaWisp getScelta() {
        return scelta;
    }

    /**
     * Sets the value of the scelta property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoSceltaWisp }
     *     
     */
    public void setScelta(TipoSceltaWisp value) {
        this.scelta = value;
    }

    /**
     * Gets the value of the canale property.
     * 
     * @return
     *     possible object is
     *     {@link Canale }
     *     
     */
    public Canale getCanale() {
        return canale;
    }

    /**
     * Sets the value of the canale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Canale }
     *     
     */
    public void setCanale(Canale value) {
        this.canale = value;
    }

}
