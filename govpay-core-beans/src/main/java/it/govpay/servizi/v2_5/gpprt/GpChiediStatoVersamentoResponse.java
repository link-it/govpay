
package it.govpay.servizi.v2_5.gpprt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.v2_3.commons.GpResponse;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="numeroAvviso" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="barCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoVersamento"/&gt;
 *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo" minOccurs="0"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string140"/&gt;
 *           &lt;element name="spezzoneCausale" type="{http://www.govpay.it/servizi/commons/}string35" maxOccurs="6"/&gt;
 *           &lt;element name="spezzoneCausaleStrutturata" maxOccurs="6"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string20"/&gt;
 *                     &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
    "codDominio",
    "iuv",
    "numeroAvviso",
    "qrCode",
    "barCode",
    "stato",
    "importoTotale",
    "causale",
    "spezzoneCausale",
    "spezzoneCausaleStrutturata",
    "dataScadenza",
    "transazione"
})
@XmlRootElement(name = "gpChiediStatoVersamentoResponse")
public class GpChiediStatoVersamentoResponse
    extends GpResponse
{

    protected String codApplicazione;
    protected String codVersamentoEnte;
    protected String codDominio;
    protected String iuv;
    protected String numeroAvviso;
    protected byte[] qrCode;
    protected byte[] barCode;
    @XmlSchemaType(name = "string")
    protected StatoVersamento stato;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotale;
    protected String causale;
    protected List<String> spezzoneCausale;
    protected List<GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataScadenza;
    protected List<Transazione> transazione;

    /**
     * Recupera il valore della proprietà codApplicazione.
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
     * Imposta il valore della proprietà codApplicazione.
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
     * Recupera il valore della proprietà codVersamentoEnte.
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
     * Imposta il valore della proprietà codVersamentoEnte.
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
     * Recupera il valore della proprietà codDominio.
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
        return iuv;
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
     * Recupera il valore della proprietà numeroAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroAvviso() {
        return numeroAvviso;
    }

    /**
     * Imposta il valore della proprietà numeroAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroAvviso(String value) {
        this.numeroAvviso = value;
    }

    /**
     * Recupera il valore della proprietà qrCode.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getQrCode() {
        return qrCode;
    }

    /**
     * Imposta il valore della proprietà qrCode.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setQrCode(byte[] value) {
        this.qrCode = value;
    }

    /**
     * Recupera il valore della proprietà barCode.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBarCode() {
        return barCode;
    }

    /**
     * Imposta il valore della proprietà barCode.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBarCode(byte[] value) {
        this.barCode = value;
    }

    /**
     * Recupera il valore della proprietà stato.
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
     * Imposta il valore della proprietà stato.
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
     * Recupera il valore della proprietà importoTotale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoTotale() {
        return importoTotale;
    }

    /**
     * Imposta il valore della proprietà importoTotale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoTotale(BigDecimal value) {
        this.importoTotale = value;
    }

    /**
     * Recupera il valore della proprietà causale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausale() {
        return causale;
    }

    /**
     * Imposta il valore della proprietà causale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausale(String value) {
        this.causale = value;
    }

    /**
     * Gets the value of the spezzoneCausale property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spezzoneCausale property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpezzoneCausale().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSpezzoneCausale() {
        if (spezzoneCausale == null) {
            spezzoneCausale = new ArrayList<String>();
        }
        return this.spezzoneCausale;
    }

    /**
     * Gets the value of the spezzoneCausaleStrutturata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spezzoneCausaleStrutturata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpezzoneCausaleStrutturata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata }
     * 
     * 
     */
    public List<GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
        if (spezzoneCausaleStrutturata == null) {
            spezzoneCausaleStrutturata = new ArrayList<GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata>();
        }
        return this.spezzoneCausaleStrutturata;
    }

    /**
     * Recupera il valore della proprietà dataScadenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * Imposta il valore della proprietà dataScadenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataScadenza(Date value) {
        this.dataScadenza = value;
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
     *         &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string20"/&gt;
     *         &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
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
        "causale",
        "importo"
    })
    public static class SpezzoneCausaleStrutturata {

        @XmlElement(required = true)
        protected String causale;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importo;

        /**
         * Recupera il valore della proprietà causale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCausale() {
            return causale;
        }

        /**
         * Imposta il valore della proprietà causale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCausale(String value) {
            this.causale = value;
        }

        /**
         * Recupera il valore della proprietà importo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImporto() {
            return importo;
        }

        /**
         * Imposta il valore della proprietà importo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImporto(BigDecimal value) {
            this.importo = value;
        }

    }

}
