
package it.govpay.servizi.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.IuvGenerato;


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
     * Gets the value of the iuvGenerato property.
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
     * Sets the value of the iuvGenerato property.
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
