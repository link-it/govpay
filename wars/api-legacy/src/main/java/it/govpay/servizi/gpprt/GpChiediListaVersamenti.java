
package it.govpay.servizi.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.StatoVersamento;


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
 *         &lt;element name="codUnivocoDebitore" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoVersamento" maxOccurs="4" minOccurs="0"/&gt;
 *         &lt;element name="ordinamento"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="DATA_SCADENZA_ASC"/&gt;
 *               &lt;enumeration value="DATA_SCADENZA_DES"/&gt;
 *               &lt;enumeration value="DATA_CARICAMENTO_ASC"/&gt;
 *               &lt;enumeration value="DATA_CARICAMENTO_DES"/&gt;
 *               &lt;enumeration value="DATA_AGGIORNAMENTO_ASC"/&gt;
 *               &lt;enumeration value="DATA_AGGIORNAMENTO_DES"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
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
    "codPortale",
    "codUnivocoDebitore",
    "stato",
    "ordinamento"
})
@XmlRootElement(name = "gpChiediListaVersamenti")
public class GpChiediListaVersamenti {

    @XmlElement(required = true)
    protected String codPortale;
    @XmlElement(required = true)
    protected String codUnivocoDebitore;
    @XmlSchemaType(name = "string")
    protected List<StatoVersamento> stato;
    @XmlElement(required = true)
    protected String ordinamento;

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

    /**
     * Gets the value of the stato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatoVersamento }
     * 
     * 
     */
    public List<StatoVersamento> getStato() {
        if (stato == null) {
            stato = new ArrayList<StatoVersamento>();
        }
        return this.stato;
    }

    /**
     * Gets the value of the ordinamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdinamento() {
        return ordinamento;
    }

    /**
     * Sets the value of the ordinamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdinamento(String value) {
        this.ordinamento = value;
    }

}
