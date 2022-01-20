
package it.govpay.servizi.gpprt;

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
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *             &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *             &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="bundleKey" type="{http://www.govpay.it/servizi/commons/}string256"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="codUnivocoDebitore" type="{http://www.govpay.it/servizi/commons/}string35" minOccurs="0"/&gt;
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
    "codApplicazione",
    "codVersamentoEnte",
    "codDominio",
    "iuv",
    "bundleKey",
    "codUnivocoDebitore"
})
@XmlRootElement(name = "gpChiediStatoVersamento")
public class GpChiediStatoVersamento {

    @XmlElement(required = true)
    protected String codPortale;
    protected String codApplicazione;
    protected String codVersamentoEnte;
    protected String codDominio;
    protected String iuv;
    protected String bundleKey;
    protected String codUnivocoDebitore;

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
     * Gets the value of the bundleKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundleKey() {
        return bundleKey;
    }

    /**
     * Sets the value of the bundleKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundleKey(String value) {
        this.bundleKey = value;
    }

    /**
     * Gets the value of the codUnivocoDebitore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnivocoDebitore() {
        return codUnivocoDebitore;
    }

    /**
     * Sets the value of the codUnivocoDebitore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUnivocoDebitore(String value) {
        this.codUnivocoDebitore = value;
    }

}
