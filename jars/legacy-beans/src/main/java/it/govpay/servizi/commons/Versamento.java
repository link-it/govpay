
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Java class for versamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codUnitaOperativa" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="debitore" type="{http://www.govpay.it/servizi/commons/}anagrafica"/&gt;
 *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *         &lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="aggiornabile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codDebito" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="annoTributario" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="bundlekey" type="{http://www.govpay.it/servizi/commons/}string256" minOccurs="0"/&gt;
 *         &lt;choice&gt;
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
 *         &lt;element name="singoloVersamento" maxOccurs="5"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                   &lt;element name="note" type="{http://www.govpay.it/servizi/commons/}string512" minOccurs="0"/&gt;
 *                   &lt;choice&gt;
 *                     &lt;element name="codTributo" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                     &lt;element name="bolloTelematico"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="tipo" type="{http://www.govpay.it/servizi/commons/}tipoBollo"/&gt;
 *                               &lt;element name="hash" type="{http://www.govpay.it/servizi/commons/}string70"/&gt;
 *                               &lt;element name="provincia" type="{http://www.govpay.it/servizi/commons/}string2"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="tributo"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="ibanAccredito" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                               &lt;element name="ibanAppoggio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                               &lt;element name="tipoContabilita" type="{http://www.govpay.it/servizi/commons/}tipoContabilita"/&gt;
 *                               &lt;element name="codContabilita"&gt;
 *                                 &lt;simpleType&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                     &lt;pattern value="\S{3,138}"/&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/simpleType&gt;
 *                               &lt;/element&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/choice&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
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
@XmlType(name = "versamento", propOrder = {
    "codApplicazione",
    "codVersamentoEnte",
    "iuv",
    "codDominio",
    "codUnitaOperativa",
    "debitore",
    "importoTotale",
    "dataScadenza",
    "aggiornabile",
    "codDebito",
    "annoTributario",
    "bundlekey",
    "causale",
    "spezzoneCausale",
    "spezzoneCausaleStrutturata",
    "singoloVersamento"
})
public class Versamento {

    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true)
    protected String codVersamentoEnte;
    protected String iuv;
    @XmlElement(required = true)
    protected String codDominio;
    protected String codUnitaOperativa;
    @XmlElement(required = true)
    protected Anagrafica debitore;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotale;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataScadenza;
    protected Boolean aggiornabile;
    protected String codDebito;
    protected Integer annoTributario;
    protected String bundlekey;
    protected String causale;
    protected List<String> spezzoneCausale;
    protected List<Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata;
    @XmlElement(required = true)
    protected List<Versamento.SingoloVersamento> singoloVersamento;

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
     * Gets the value of the codUnitaOperativa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnitaOperativa() {
        return codUnitaOperativa;
    }

    /**
     * Sets the value of the codUnitaOperativa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUnitaOperativa(String value) {
        this.codUnitaOperativa = value;
    }

    /**
     * Gets the value of the debitore property.
     * 
     * @return
     *     possible object is
     *     {@link Anagrafica }
     *     
     */
    public Anagrafica getDebitore() {
        return debitore;
    }

    /**
     * Sets the value of the debitore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Anagrafica }
     *     
     */
    public void setDebitore(Anagrafica value) {
        this.debitore = value;
    }

    /**
     * Gets the value of the importoTotale property.
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
     * Sets the value of the importoTotale property.
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
     * Gets the value of the dataScadenza property.
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
     * Sets the value of the dataScadenza property.
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
     * Gets the value of the aggiornabile property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAggiornabile() {
        return aggiornabile;
    }

    /**
     * Sets the value of the aggiornabile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAggiornabile(Boolean value) {
        this.aggiornabile = value;
    }

    /**
     * Gets the value of the codDebito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDebito() {
        return codDebito;
    }

    /**
     * Sets the value of the codDebito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDebito(String value) {
        this.codDebito = value;
    }

    /**
     * Gets the value of the annoTributario property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnnoTributario() {
        return annoTributario;
    }

    /**
     * Sets the value of the annoTributario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnnoTributario(Integer value) {
        this.annoTributario = value;
    }

    /**
     * Gets the value of the bundlekey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundlekey() {
        return bundlekey;
    }

    /**
     * Sets the value of the bundlekey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundlekey(String value) {
        this.bundlekey = value;
    }

    /**
     * Gets the value of the causale property.
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
     * Sets the value of the causale property.
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
     * {@link Versamento.SpezzoneCausaleStrutturata }
     * 
     * 
     */
    public List<Versamento.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
        if (spezzoneCausaleStrutturata == null) {
            spezzoneCausaleStrutturata = new ArrayList<Versamento.SpezzoneCausaleStrutturata>();
        }
        return this.spezzoneCausaleStrutturata;
    }

    /**
     * Gets the value of the singoloVersamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the singoloVersamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSingoloVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Versamento.SingoloVersamento }
     * 
     * 
     */
    public List<Versamento.SingoloVersamento> getSingoloVersamento() {
        if (singoloVersamento == null) {
            singoloVersamento = new ArrayList<Versamento.SingoloVersamento>();
        }
        return this.singoloVersamento;
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
     *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
     *         &lt;element name="note" type="{http://www.govpay.it/servizi/commons/}string512" minOccurs="0"/&gt;
     *         &lt;choice&gt;
     *           &lt;element name="codTributo" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *           &lt;element name="bolloTelematico"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="tipo" type="{http://www.govpay.it/servizi/commons/}tipoBollo"/&gt;
     *                     &lt;element name="hash" type="{http://www.govpay.it/servizi/commons/}string70"/&gt;
     *                     &lt;element name="provincia" type="{http://www.govpay.it/servizi/commons/}string2"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *           &lt;element name="tributo"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="ibanAccredito" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *                     &lt;element name="ibanAppoggio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *                     &lt;element name="tipoContabilita" type="{http://www.govpay.it/servizi/commons/}tipoContabilita"/&gt;
     *                     &lt;element name="codContabilita"&gt;
     *                       &lt;simpleType&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                           &lt;pattern value="\S{3,138}"/&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/simpleType&gt;
     *                     &lt;/element&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
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
        "codSingoloVersamentoEnte",
        "importo",
        "note",
        "codTributo",
        "bolloTelematico",
        "tributo"
    })
    public static class SingoloVersamento {

        @XmlElement(required = true)
        protected String codSingoloVersamentoEnte;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importo;
        protected String note;
        protected String codTributo;
        protected Versamento.SingoloVersamento.BolloTelematico bolloTelematico;
        protected Versamento.SingoloVersamento.Tributo tributo;

        /**
         * Gets the value of the codSingoloVersamentoEnte property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodSingoloVersamentoEnte() {
            return codSingoloVersamentoEnte;
        }

        /**
         * Sets the value of the codSingoloVersamentoEnte property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodSingoloVersamentoEnte(String value) {
            this.codSingoloVersamentoEnte = value;
        }

        /**
         * Gets the value of the importo property.
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
         * Sets the value of the importo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImporto(BigDecimal value) {
            this.importo = value;
        }

        /**
         * Gets the value of the note property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Sets the value of the note property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

        /**
         * Gets the value of the codTributo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodTributo() {
            return codTributo;
        }

        /**
         * Sets the value of the codTributo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodTributo(String value) {
            this.codTributo = value;
        }

        /**
         * Gets the value of the bolloTelematico property.
         * 
         * @return
         *     possible object is
         *     {@link Versamento.SingoloVersamento.BolloTelematico }
         *     
         */
        public Versamento.SingoloVersamento.BolloTelematico getBolloTelematico() {
            return bolloTelematico;
        }

        /**
         * Sets the value of the bolloTelematico property.
         * 
         * @param value
         *     allowed object is
         *     {@link Versamento.SingoloVersamento.BolloTelematico }
         *     
         */
        public void setBolloTelematico(Versamento.SingoloVersamento.BolloTelematico value) {
            this.bolloTelematico = value;
        }

        /**
         * Gets the value of the tributo property.
         * 
         * @return
         *     possible object is
         *     {@link Versamento.SingoloVersamento.Tributo }
         *     
         */
        public Versamento.SingoloVersamento.Tributo getTributo() {
            return tributo;
        }

        /**
         * Sets the value of the tributo property.
         * 
         * @param value
         *     allowed object is
         *     {@link Versamento.SingoloVersamento.Tributo }
         *     
         */
        public void setTributo(Versamento.SingoloVersamento.Tributo value) {
            this.tributo = value;
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
         *         &lt;element name="tipo" type="{http://www.govpay.it/servizi/commons/}tipoBollo"/&gt;
         *         &lt;element name="hash" type="{http://www.govpay.it/servizi/commons/}string70"/&gt;
         *         &lt;element name="provincia" type="{http://www.govpay.it/servizi/commons/}string2"/&gt;
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
            "tipo",
            "hash",
            "provincia"
        })
        public static class BolloTelematico {

            @XmlElement(required = true)
            protected String tipo;
            @XmlElement(required = true)
            protected String hash;
            @XmlElement(required = true)
            protected String provincia;

            /**
             * Gets the value of the tipo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipo() {
                return tipo;
            }

            /**
             * Sets the value of the tipo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipo(String value) {
                this.tipo = value;
            }

            /**
             * Gets the value of the hash property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHash() {
                return hash;
            }

            /**
             * Sets the value of the hash property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHash(String value) {
                this.hash = value;
            }

            /**
             * Gets the value of the provincia property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProvincia() {
                return provincia;
            }

            /**
             * Sets the value of the provincia property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProvincia(String value) {
                this.provincia = value;
            }

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
         *         &lt;element name="ibanAccredito" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
         *         &lt;element name="ibanAppoggio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
         *         &lt;element name="tipoContabilita" type="{http://www.govpay.it/servizi/commons/}tipoContabilita"/&gt;
         *         &lt;element name="codContabilita"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;pattern value="\S{3,138}"/&gt;
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
            "ibanAccredito",
            "ibanAppoggio",
            "tipoContabilita",
            "codContabilita"
        })
        public static class Tributo {

            @XmlElement(required = true)
            protected String ibanAccredito;
            protected String ibanAppoggio;
            @XmlElement(required = true)
            @XmlSchemaType(name = "string")
            protected TipoContabilita tipoContabilita;
            @XmlElement(required = true)
            protected String codContabilita;

            /**
             * Gets the value of the ibanAccredito property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIbanAccredito() {
                return ibanAccredito;
            }

            /**
             * Sets the value of the ibanAccredito property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIbanAccredito(String value) {
                this.ibanAccredito = value;
            }

            /**
             * Gets the value of the ibanAppoggio property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIbanAppoggio() {
                return ibanAppoggio;
            }

            /**
             * Sets the value of the ibanAppoggio property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIbanAppoggio(String value) {
                this.ibanAppoggio = value;
            }

            /**
             * Gets the value of the tipoContabilita property.
             * 
             * @return
             *     possible object is
             *     {@link TipoContabilita }
             *     
             */
            public TipoContabilita getTipoContabilita() {
                return tipoContabilita;
            }

            /**
             * Sets the value of the tipoContabilita property.
             * 
             * @param value
             *     allowed object is
             *     {@link TipoContabilita }
             *     
             */
            public void setTipoContabilita(TipoContabilita value) {
                this.tipoContabilita = value;
            }

            /**
             * Gets the value of the codContabilita property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodContabilita() {
                return codContabilita;
            }

            /**
             * Sets the value of the codContabilita property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodContabilita(String value) {
                this.codContabilita = value;
            }

        }

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
         * Gets the value of the causale property.
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
         * Sets the value of the causale property.
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
         * Gets the value of the importo property.
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
         * Sets the value of the importo property.
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
