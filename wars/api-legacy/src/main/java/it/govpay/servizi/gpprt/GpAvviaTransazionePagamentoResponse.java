
package it.govpay.servizi.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="urlRedirect" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="pspSessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rifTransazione" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
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
    "urlRedirect",
    "pspSessionId",
    "rifTransazione"
})
@XmlRootElement(name = "gpAvviaTransazionePagamentoResponse")
public class GpAvviaTransazionePagamentoResponse
    extends GpResponse
{

    @XmlSchemaType(name = "anyURI")
    protected String urlRedirect;
    protected String pspSessionId;
    protected List<GpAvviaTransazionePagamentoResponse.RifTransazione> rifTransazione;

    /**
     * Gets the value of the urlRedirect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRedirect() {
        return urlRedirect;
    }

    /**
     * Sets the value of the urlRedirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRedirect(String value) {
        this.urlRedirect = value;
    }

    /**
     * Gets the value of the pspSessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPspSessionId() {
        return pspSessionId;
    }

    /**
     * Sets the value of the pspSessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPspSessionId(String value) {
        this.pspSessionId = value;
    }

    /**
     * Gets the value of the rifTransazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rifTransazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRifTransazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpAvviaTransazionePagamentoResponse.RifTransazione }
     * 
     * 
     */
    public List<GpAvviaTransazionePagamentoResponse.RifTransazione> getRifTransazione() {
        if (rifTransazione == null) {
            rifTransazione = new ArrayList<GpAvviaTransazionePagamentoResponse.RifTransazione>();
        }
        return this.rifTransazione;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codApplicazione",
        "codVersamentoEnte",
        "codDominio",
        "iuv",
        "ccp"
    })
    public static class RifTransazione {

        @XmlElement(required = true)
        protected String codApplicazione;
        @XmlElement(required = true)
        protected String codVersamentoEnte;
        @XmlElement(required = true)
        protected String codDominio;
        @XmlElement(required = true)
        protected String iuv;
        @XmlElement(required = true)
        protected String ccp;

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
         * Gets the value of the codVersamentoEnte property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodVersamentoEnte() {
            return codVersamentoEnte;
        }

        /**
         * Sets the value of the codVersamentoEnte property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodVersamentoEnte(String value) {
            this.codVersamentoEnte = value;
        }

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

        /**
         * Gets the value of the iuv property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIuv() {
            return iuv;
        }

        /**
         * Sets the value of the iuv property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIuv(String value) {
            this.iuv = value;
        }

        /**
         * Gets the value of the ccp property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCcp() {
            return ccp;
        }

        /**
         * Sets the value of the ccp property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCcp(String value) {
            this.ccp = value;
        }

    }

}
