
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
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codKeyPA" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
 *         &lt;element name="codKeyWISP" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
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
    "codKeyPA",
    "codKeyWISP"
})
@XmlRootElement(name = "gpChiediSceltaWisp")
public class GpChiediSceltaWisp {

    @XmlElement(required = true)
    protected String codPortale;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String codKeyPA;
    @XmlElement(required = true)
    protected String codKeyWISP;

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
     * Gets the value of the codKeyPA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodKeyPA() {
        return codKeyPA;
    }

    /**
     * Sets the value of the codKeyPA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodKeyPA(String value) {
        this.codKeyPA = value;
    }

    /**
     * Gets the value of the codKeyWISP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodKeyWISP() {
        return codKeyWISP;
    }

    /**
     * Sets the value of the codKeyWISP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodKeyWISP(String value) {
        this.codKeyWISP = value;
    }

}
