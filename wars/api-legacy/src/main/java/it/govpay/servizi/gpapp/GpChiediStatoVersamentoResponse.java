
package it.govpay.servizi.gpapp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.Transazione;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoVersamento"/&gt;
 *         &lt;element name="transazione" type="{http://www.govpay.it/servizi/commons/}transazione" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "codApplicazione",
    "codVersamentoEnte",
    "stato",
    "transazione"
})
@XmlRootElement(name = "gpChiediStatoVersamentoResponse")
public class GpChiediStatoVersamentoResponse
    extends GpResponse
{

    protected String codApplicazione;
    protected String codVersamentoEnte;
    @XmlSchemaType(name = "string")
    protected StatoVersamento stato;
    protected List<Transazione> transazione;

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
     * Gets the value of the stato property.
     * 
     * @return
     *     possible object is
     *     {@link StatoVersamento }
     *     
     */
    public StatoVersamento getStato() {
        return stato;
    }

    /**
     * Sets the value of the stato property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoVersamento }
     *     
     */
    public void setStato(StatoVersamento value) {
        this.stato = value;
    }

    /**
     * Gets the value of the transazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transazione }
     * 
     * 
     */
    public List<Transazione> getTransazione() {
        if (transazione == null) {
            transazione = new ArrayList<Transazione>();
        }
        return this.transazione;
    }

}
