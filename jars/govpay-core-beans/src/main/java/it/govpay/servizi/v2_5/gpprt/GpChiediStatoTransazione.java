
package it.govpay.servizi.v2_5.gpprt;

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
    "ccp"
})
@XmlRootElement(name = "gpChiediStatoTransazione")
public class GpChiediStatoTransazione {

    @XmlElement(required = true)
    protected String codPortale;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String iuv;
    @XmlElement(required = true)
    protected String ccp;

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

}
