
package it.govpay.servizi.v2_5.gpprt;

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
     * Recupera il valore della proprietà codUnivocoDebitore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnivocoDebitore() {
        return this.codUnivocoDebitore;
    }

    /**
     * Imposta il valore della proprietà codUnivocoDebitore.
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
        if (this.stato == null) {
            this.stato = new ArrayList<>();
        }
        return this.stato;
    }

    /**
     * Recupera il valore della proprietà ordinamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdinamento() {
        return this.ordinamento;
    }

    /**
     * Imposta il valore della proprietà ordinamento.
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
