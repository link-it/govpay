
package it.govpay.servizi.pa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà codSessionePortale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSessionePortale() {
        return this.codSessionePortale;
    }

    /**
     * Imposta il valore della proprietà codSessionePortale.
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
     * Recupera il valore della proprietà codApplicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodApplicazione() {
        return this.codApplicazione;
    }

    /**
     * Imposta il valore della proprietà codApplicazione.
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
     * Recupera il valore della proprietà codVersamentoEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodVersamentoEnte() {
        return this.codVersamentoEnte;
    }

    /**
     * Imposta il valore della proprietà codVersamentoEnte.
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
     * Recupera il valore della proprietà richiestaStorno.
     * 
     * @return
     *     possible object is
     *     {@link PaNotificaStorno.RichiestaStorno }
     *     
     */
    public PaNotificaStorno.RichiestaStorno getRichiestaStorno() {
        return this.richiestaStorno;
    }

    /**
     * Imposta il valore della proprietà richiestaStorno.
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
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
         * Recupera il valore della proprietà codDominio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodDominio() {
            return this.codDominio;
        }

        /**
         * Imposta il valore della proprietà codDominio.
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
         * Recupera il valore della proprietà iuv.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIuv() {
            return this.iuv;
        }

        /**
         * Imposta il valore della proprietà iuv.
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
         * Recupera il valore della proprietà ccp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCcp() {
            return this.ccp;
        }

        /**
         * Imposta il valore della proprietà ccp.
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
