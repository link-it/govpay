
package it.govpay.servizi.gpapp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.EstremiFlussoRendicontazione;
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
 *         &lt;element name="flussoRendicontazione" type="{http://www.govpay.it/servizi/commons/}estremiFlussoRendicontazione" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "gpChiediListaFlussiRendicontazioneResponse")
public class GpChiediListaFlussiRendicontazioneResponse
    extends GpResponse
{

    protected String codApplicazione;
    protected List<EstremiFlussoRendicontazione> flussoRendicontazione;

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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flussoRendicontazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlussoRendicontazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EstremiFlussoRendicontazione }
     * 
     * 
     */
    public List<EstremiFlussoRendicontazione> getFlussoRendicontazione() {
        if (flussoRendicontazione == null) {
            flussoRendicontazione = new ArrayList<EstremiFlussoRendicontazione>();
        }
        return this.flussoRendicontazione;
    }

}
