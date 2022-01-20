
package it.govpay.servizi.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.FlussoRendicontazione;
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
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="flussoRendicontazione" type="{http://www.govpay.it/servizi/commons/}flussoRendicontazione"/&gt;
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
    "codApplicazione",
    "flussoRendicontazione"
})
@XmlRootElement(name = "gpChiediFlussoRendicontazioneResponse")
public class GpChiediFlussoRendicontazioneResponse
    extends GpResponse
{

    protected String codApplicazione;
    protected FlussoRendicontazione flussoRendicontazione;

    /**
     * Gets the value of the codApplicazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodApplicazione() {
        return codApplicazione;
    }

    /**
     * Sets the value of the codApplicazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodApplicazione(String value) {
        this.codApplicazione = value;
    }

    /**
     * Gets the value of the flussoRendicontazione property.
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
     * Sets the value of the flussoRendicontazione property.
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
