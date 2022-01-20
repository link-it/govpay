
package it.govpay.servizi.pa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="codSessionePortale" type="{http://www.govpay.it/servizi/commons/}uuid" minOccurs="0"/&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="richiestaStorno"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.govpay.it/servizi/commons/}richiestaStorno"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "codSessionePortale",
    "codApplicazione",
    "codVersamentoEnte",
    "richiestaStorno"
})
@XmlRootElement(name = "paNotificaStorno")
public class PaNotificaStorno {

    protected String codSessionePortale;
    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true)
    protected String codVersamentoEnte;
    @XmlElement(required = true)
    protected PaNotificaStorno.RichiestaStorno richiestaStorno;

    /**
     * Gets the value of the codSessionePortale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSessionePortale() {
        return codSessionePortale;
    }

    /**
     * Sets the value of the codSessionePortale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSessionePortale(String value) {
        this.codSessionePortale = value;
    }

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
     * Gets the value of the richiestaStorno property.
     * 
     * @return
     *     possible object is
     *     {@link PaNotificaStorno.RichiestaStorno }
     *     
     */
    public PaNotificaStorno.RichiestaStorno getRichiestaStorno() {
        return richiestaStorno;
    }

    /**
     * Sets the value of the richiestaStorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaNotificaStorno.RichiestaStorno }
     *     
     */
    public void setRichiestaStorno(PaNotificaStorno.RichiestaStorno value) {
        this.richiestaStorno = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.govpay.it/servizi/commons/}richiestaStorno"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
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
        "codDominio",
        "iuv",
        "ccp"
    })
    public static class RichiestaStorno
        extends it.govpay.servizi.commons.RichiestaStorno
    {

        @XmlElement(required = true)
        protected String codDominio;
        @XmlElement(required = true)
        protected String iuv;
        @XmlElement(required = true)
        protected String ccp;

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
