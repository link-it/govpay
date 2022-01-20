
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
 *         &lt;element name="codRichiestaStorno" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
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
    "codRichiestaStorno"
})
@XmlRootElement(name = "gpChiediStatoRichiestaStorno")
public class GpChiediStatoRichiestaStorno {

    @XmlElement(required = true)
    protected String codPortale;
    @XmlElement(required = true)
    protected String codRichiestaStorno;

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
     * Gets the value of the codRichiestaStorno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRichiestaStorno() {
        return codRichiestaStorno;
    }

    /**
     * Sets the value of the codRichiestaStorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRichiestaStorno(String value) {
        this.codRichiestaStorno = value;
    }

}
