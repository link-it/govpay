
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
 * <p>Classe Java per versamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà codUnitaOperativa.
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
     * Imposta il valore della proprietà codUnitaOperativa.
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
     * Recupera il valore della proprietà debitore.
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
     * Imposta il valore della proprietà debitore.
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
     * Recupera il valore della proprietà aggiornabile.
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
     * Imposta il valore della proprietà aggiornabile.
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
     * Recupera il valore della proprietà codDebito.
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
     * Imposta il valore della proprietà codDebito.
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
     * Recupera il valore della proprietà annoTributario.
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
     * Imposta il valore della proprietà annoTributario.
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
     * Recupera il valore della proprietà bundlekey.
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
     * Imposta il valore della proprietà bundlekey.
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
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
         * Recupera il valore della proprietà codSingoloVersamentoEnte.
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
         * Imposta il valore della proprietà codSingoloVersamentoEnte.
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

        /**
         * Recupera il valore della proprietà note.
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
         * Imposta il valore della proprietà note.
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
         * Recupera il valore della proprietà codTributo.
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
         * Imposta il valore della proprietà codTributo.
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
         * Recupera il valore della proprietà bolloTelematico.
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
         * Imposta il valore della proprietà bolloTelematico.
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
         * Recupera il valore della proprietà tributo.
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
         * Imposta il valore della proprietà tributo.
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
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
             * Recupera il valore della proprietà tipo.
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
             * Imposta il valore della proprietà tipo.
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
             * Recupera il valore della proprietà hash.
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
             * Imposta il valore della proprietà hash.
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
             * Recupera il valore della proprietà provincia.
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
             * Imposta il valore della proprietà provincia.
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
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="ibanAccredito" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
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
            "tipoContabilita",
            "codContabilita"
        })
        public static class Tributo {

            @XmlElement(required = true)
            protected String ibanAccredito;
            @XmlElement(required = true)
            @XmlSchemaType(name = "string")
            protected TipoContabilita tipoContabilita;
            @XmlElement(required = true)
            protected String codContabilita;

            /**
             * Recupera il valore della proprietà ibanAccredito.
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
             * Imposta il valore della proprietà ibanAccredito.
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
             * Recupera il valore della proprietà tipoContabilita.
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
             * Imposta il valore della proprietà tipoContabilita.
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
             * Recupera il valore della proprietà codContabilita.
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
             * Imposta il valore della proprietà codContabilita.
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
