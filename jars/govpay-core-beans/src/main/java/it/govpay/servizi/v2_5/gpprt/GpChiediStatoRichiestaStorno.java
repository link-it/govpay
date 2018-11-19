
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
     * Recupera il valore della proprietà codRichiestaStorno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRichiestaStorno() {
        return this.codRichiestaStorno;
    }

    /**
     * Imposta il valore della proprietà codRichiestaStorno.
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
