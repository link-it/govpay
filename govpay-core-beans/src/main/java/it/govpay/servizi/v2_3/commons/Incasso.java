
package it.govpay.servizi.v2_3.commons;

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
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per incasso complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="incasso"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="trn" type="{http://www.govpay.it/servizi/commons/}string512"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string512"/&gt;
 *         &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
 *         &lt;element name="dataValuta" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dataContabile" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dispositivo" type="{http://www.govpay.it/servizi/commons/}string256" minOccurs="0"/&gt;
 *         &lt;element name="pagamento" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="importoPagato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                   &lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
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
@XmlType(name = "incasso", propOrder = {
    "trn",
    "codDominio",
    "causale",
    "importo",
    "dataValuta",
    "dataContabile",
    "dispositivo",
    "pagamento"
})
public class Incasso {

    @XmlElement(required = true)
    protected String trn;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String causale;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importo;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataValuta;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataContabile;
    protected String dispositivo;
    @XmlElement(required = true)
    protected List<Incasso.Pagamento> pagamento;

    /**
     * Recupera il valore della proprietà trn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrn() {
        return trn;
    }

    /**
     * Imposta il valore della proprietà trn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrn(String value) {
        this.trn = value;
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

    /**
     * Recupera il valore della proprietà dataValuta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataValuta() {
        return dataValuta;
    }

    /**
     * Imposta il valore della proprietà dataValuta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataValuta(Date value) {
        this.dataValuta = value;
    }

    /**
     * Recupera il valore della proprietà dataContabile.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataContabile() {
        return dataContabile;
    }

    /**
     * Imposta il valore della proprietà dataContabile.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataContabile(Date value) {
        this.dataContabile = value;
    }

    /**
     * Recupera il valore della proprietà dispositivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispositivo() {
        return dispositivo;
    }

    /**
     * Imposta il valore della proprietà dispositivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispositivo(String value) {
        this.dispositivo = value;
    }

    /**
     * Gets the value of the pagamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pagamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPagamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Incasso.Pagamento }
     * 
     * 
     */
    public List<Incasso.Pagamento> getPagamento() {
        if (pagamento == null) {
            pagamento = new ArrayList<Incasso.Pagamento>();
        }
        return this.pagamento;
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
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="importoPagato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
     *         &lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
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
        "iuv",
        "iur",
        "codApplicazione",
        "codVersamentoEnte",
        "codSingoloVersamentoEnte",
        "importoPagato",
        "dataPagamento"
    })
    public static class Pagamento {

        @XmlElement(required = true)
        protected String iuv;
        @XmlElement(required = true)
        protected String iur;
        @XmlElement(required = true)
        protected String codApplicazione;
        @XmlElement(required = true)
        protected String codVersamentoEnte;
        @XmlElement(required = true)
        protected String codSingoloVersamentoEnte;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importoPagato;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter4 .class)
        @XmlSchemaType(name = "date")
        protected Date dataPagamento;

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
         * Recupera il valore della proprietà iur.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIur() {
            return iur;
        }

        /**
         * Imposta il valore della proprietà iur.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIur(String value) {
            this.iur = value;
        }

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
         * Recupera il valore della proprietà importoPagato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImportoPagato() {
            return importoPagato;
        }

        /**
         * Imposta il valore della proprietà importoPagato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImportoPagato(BigDecimal value) {
            this.importoPagato = value;
        }

        /**
         * Recupera il valore della proprietà dataPagamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getDataPagamento() {
            return dataPagamento;
        }

        /**
         * Imposta il valore della proprietà dataPagamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataPagamento(Date value) {
            this.dataPagamento = value;
        }

    }

}
