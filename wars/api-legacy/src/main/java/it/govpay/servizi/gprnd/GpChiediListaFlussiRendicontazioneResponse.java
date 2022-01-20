
package it.govpay.servizi.gprnd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="flussoRendicontazione"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.govpay.it/servizi/commons/}estremiFlussoRendicontazione"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
@XmlRootElement(name = "gpChiediListaFlussiRendicontazioneResponse")
public class GpChiediListaFlussiRendicontazioneResponse
    extends GpResponse
{

    protected List<GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione> flussoRendicontazione;

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
     * {@link GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione }
     * 
     * 
     */
    public List<GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione> getFlussoRendicontazione() {
        if (flussoRendicontazione == null) {
            flussoRendicontazione = new ArrayList<GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione>();
        }
        return this.flussoRendicontazione;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.govpay.it/servizi/commons/}estremiFlussoRendicontazione"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
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
        "codDominio"
    })
    public static class FlussoRendicontazione
        extends EstremiFlussoRendicontazione
    {

        @XmlElement(required = true)
        protected String codDominio;

        /**
         * Gets the value of the codDominio property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodDominio() {
            return codDominio;
        }

        /**
         * Sets the value of the codDominio property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodDominio(String value) {
            this.codDominio = value;
        }

    }

}
