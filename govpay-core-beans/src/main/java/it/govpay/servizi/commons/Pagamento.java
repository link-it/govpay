
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per pagamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="pagamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="importoPagato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="dataAcquisizione" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="commissioniPsp" type="{http://www.govpay.it/servizi/commons/}importo" minOccurs="0"/&gt;
 *         &lt;element name="allegato" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="tipo" type="{http://www.govpay.it/servizi/commons/}tipoAllegato"/&gt;
 *                   &lt;element name="testo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dataAcquisizioneRevoca" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="causaleRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datiRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="importoRevocato" type="{http://www.govpay.it/servizi/commons/}importo" minOccurs="0"/&gt;
 *         &lt;element name="esitoRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datiEsitoRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagamento", propOrder = {
    "codSingoloVersamentoEnte",
    "importoPagato",
    "iur",
    "dataPagamento",
    "dataAcquisizione",
    "commissioniPsp",
    "allegato",
    "dataAcquisizioneRevoca",
    "causaleRevoca",
    "datiRevoca",
    "importoRevocato",
    "esitoRevoca",
    "datiEsitoRevoca"
})
public class Pagamento {

    @XmlElement(required = true)
    protected String codSingoloVersamentoEnte;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoPagato;
    @XmlElement(required = true)
    protected String iur;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataPagamento;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataAcquisizione;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal commissioniPsp;
    protected Pagamento.Allegato allegato;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataAcquisizioneRevoca;
    protected String causaleRevoca;
    protected String datiRevoca;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoRevocato;
    protected String esitoRevoca;
    protected String datiEsitoRevoca;

    /**
     * Recupera il valore della proprietà codSingoloVersamentoEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSingoloVersamentoEnte() {
        return this.codSingoloVersamentoEnte;
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
        return this.importoPagato;
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
     * Recupera il valore della proprietà iur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIur() {
        return this.iur;
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
     * Recupera il valore della proprietà dataPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataPagamento() {
        return this.dataPagamento;
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

    /**
     * Recupera il valore della proprietà dataAcquisizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizione() {
        return this.dataAcquisizione;
    }

    /**
     * Imposta il valore della proprietà dataAcquisizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAcquisizione(Date value) {
        this.dataAcquisizione = value;
    }

    /**
     * Recupera il valore della proprietà commissioniPsp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getCommissioniPsp() {
        return this.commissioniPsp;
    }

    /**
     * Imposta il valore della proprietà commissioniPsp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommissioniPsp(BigDecimal value) {
        this.commissioniPsp = value;
    }

    /**
     * Recupera il valore della proprietà allegato.
     * 
     * @return
     *     possible object is
     *     {@link Pagamento.Allegato }
     *     
     */
    public Pagamento.Allegato getAllegato() {
        return this.allegato;
    }

    /**
     * Imposta il valore della proprietà allegato.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagamento.Allegato }
     *     
     */
    public void setAllegato(Pagamento.Allegato value) {
        this.allegato = value;
    }

    /**
     * Recupera il valore della proprietà dataAcquisizioneRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizioneRevoca() {
        return this.dataAcquisizioneRevoca;
    }

    /**
     * Imposta il valore della proprietà dataAcquisizioneRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAcquisizioneRevoca(Date value) {
        this.dataAcquisizioneRevoca = value;
    }

    /**
     * Recupera il valore della proprietà causaleRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return this.causaleRevoca;
    }

    /**
     * Imposta il valore della proprietà causaleRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleRevoca(String value) {
        this.causaleRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiRevoca() {
        return this.datiRevoca;
    }

    /**
     * Imposta il valore della proprietà datiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiRevoca(String value) {
        this.datiRevoca = value;
    }

    /**
     * Recupera il valore della proprietà importoRevocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoRevocato() {
        return this.importoRevocato;
    }

    /**
     * Imposta il valore della proprietà importoRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoRevocato(BigDecimal value) {
        this.importoRevocato = value;
    }

    /**
     * Recupera il valore della proprietà esitoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitoRevoca() {
        return this.esitoRevoca;
    }

    /**
     * Imposta il valore della proprietà esitoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsitoRevoca(String value) {
        this.esitoRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiEsitoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiEsitoRevoca() {
        return this.datiEsitoRevoca;
    }

    /**
     * Imposta il valore della proprietà datiEsitoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiEsitoRevoca(String value) {
        this.datiEsitoRevoca = value;
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
     *         &lt;element name="tipo" type="{http://www.govpay.it/servizi/commons/}tipoAllegato"/&gt;
     *         &lt;element name="testo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
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
        "testo"
    })
    public static class Allegato {

        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected TipoAllegato tipo;
        @XmlElement(required = true)
        protected byte[] testo;

        /**
         * Recupera il valore della proprietà tipo.
         * 
         * @return
         *     possible object is
         *     {@link TipoAllegato }
         *     
         */
        public TipoAllegato getTipo() {
            return this.tipo;
        }

        /**
         * Imposta il valore della proprietà tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoAllegato }
         *     
         */
        public void setTipo(TipoAllegato value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della proprietà testo.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getTesto() {
            return this.testo;
        }

        /**
         * Imposta il valore della proprietà testo.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setTesto(byte[] value) {
            this.testo = value;
        }

    }

}
