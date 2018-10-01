
package it.govpay.servizi.v2_5.gpprt;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="codPortale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="causaleRevoca" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
 *             &lt;element name="datiAggiuntivi" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="pagamento" maxOccurs="5"&gt;
 *               &lt;complexType&gt;
 *                 &lt;complexContent&gt;
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                     &lt;sequence&gt;
 *                       &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                       &lt;element name="causaleRevoca" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
 *                       &lt;element name="datiAggiuntivi" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
 *                     &lt;/sequence&gt;
 *                   &lt;/restriction&gt;
 *                 &lt;/complexContent&gt;
 *               &lt;/complexType&gt;
 *             &lt;/element&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
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
    "codPortale",
    "codDominio",
    "iuv",
    "ccp",
    "causaleRevoca",
    "datiAggiuntivi",
    "pagamento"
})
@XmlRootElement(name = "gpAvviaRichiestaStorno")
public class GpAvviaRichiestaStorno {

    @XmlElement(required = true)
    protected String codPortale;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String iuv;
    @XmlElement(required = true)
    protected String ccp;
    protected String causaleRevoca;
    protected String datiAggiuntivi;
    protected List<GpAvviaRichiestaStorno.Pagamento> pagamento;

    /**
     * Recupera il valore della proprietà codPortale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPortale() {
        return this.codPortale;
    }

    /**
     * Imposta il valore della proprietà codPortale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPortale(String value) {
        this.codPortale = value;
    }

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

    /**
     * Recupera il valore della proprietà causaleRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return this.causaleRevoca;
    }

    /**
     * Imposta il valore della proprietà causaleRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleRevoca(String value) {
        this.causaleRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiAggiuntivi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiAggiuntivi() {
        return this.datiAggiuntivi;
    }

    /**
     * Imposta il valore della proprietà datiAggiuntivi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiAggiuntivi(String value) {
        this.datiAggiuntivi = value;
    }

    /**
     * Gets the value of the pagamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pagamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPagamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpAvviaRichiestaStorno.Pagamento }
     * 
     * 
     */
    public List<GpAvviaRichiestaStorno.Pagamento> getPagamento() {
        if (this.pagamento == null) {
            this.pagamento = new ArrayList<>();
        }
        return this.pagamento;
    }


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
     *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="causaleRevoca" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
     *         &lt;element name="datiAggiuntivi" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
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
        "iur",
        "causaleRevoca",
        "datiAggiuntivi"
    })
    public static class Pagamento {

        @XmlElement(required = true)
        protected String iur;
        @XmlElement(required = true)
        protected String causaleRevoca;
        @XmlElement(required = true)
        protected String datiAggiuntivi;

        /**
         * Recupera il valore della proprietà iur.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIur() {
            return this.iur;
        }

        /**
         * Imposta il valore della proprietà iur.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIur(String value) {
            this.iur = value;
        }

        /**
         * Recupera il valore della proprietà causaleRevoca.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCausaleRevoca() {
            return this.causaleRevoca;
        }

        /**
         * Imposta il valore della proprietà causaleRevoca.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCausaleRevoca(String value) {
            this.causaleRevoca = value;
        }

        /**
         * Recupera il valore della proprietà datiAggiuntivi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatiAggiuntivi() {
            return this.datiAggiuntivi;
        }

        /**
         * Imposta il valore della proprietà datiAggiuntivi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatiAggiuntivi(String value) {
            this.datiAggiuntivi = value;
        }

    }

}
