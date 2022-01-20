
package it.govpay.servizi.gpprt;

import java.util.ArrayList;
import java.util.List;
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
     * Gets the value of the codPortale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPortale() {
        return codPortale;
    }

    /**
     * Sets the value of the codPortale property.
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

    /**
     * Gets the value of the causaleRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return causaleRevoca;
    }

    /**
     * Sets the value of the causaleRevoca property.
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
     * Gets the value of the datiAggiuntivi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiAggiuntivi() {
        return datiAggiuntivi;
    }

    /**
     * Sets the value of the datiAggiuntivi property.
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
        if (pagamento == null) {
            pagamento = new ArrayList<GpAvviaRichiestaStorno.Pagamento>();
        }
        return this.pagamento;
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
         * Gets the value of the iur property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIur() {
            return iur;
        }

        /**
         * Sets the value of the iur property.
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
         * Gets the value of the causaleRevoca property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCausaleRevoca() {
            return causaleRevoca;
        }

        /**
         * Sets the value of the causaleRevoca property.
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
         * Gets the value of the datiAggiuntivi property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatiAggiuntivi() {
            return datiAggiuntivi;
        }

        /**
         * Sets the value of the datiAggiuntivi property.
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
