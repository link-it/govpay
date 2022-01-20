
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
 * <p>Java class for pagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pagamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="importoPagato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *         &lt;element name="ibanAccredito" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="ibanAppoggio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
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
    "ibanAccredito",
    "ibanAppoggio",
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
    protected String ibanAccredito;
    protected String ibanAppoggio;
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
     * Gets the value of the importoPagato property.
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
     * Sets the value of the importoPagato property.
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
     * Gets the value of the iur property.
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
     * Sets the value of the iur property.
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
     * Gets the value of the dataPagamento property.
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
     * Sets the value of the dataPagamento property.
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
     * Gets the value of the dataAcquisizione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizione() {
        return dataAcquisizione;
    }

    /**
     * Sets the value of the dataAcquisizione property.
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
     * Gets the value of the commissioniPsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getCommissioniPsp() {
        return commissioniPsp;
    }

    /**
     * Sets the value of the commissioniPsp property.
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
     * Gets the value of the allegato property.
     * 
     * @return
     *     possible object is
     *     {@link Pagamento.Allegato }
     *     
     */
    public Pagamento.Allegato getAllegato() {
        return allegato;
    }

    /**
     * Sets the value of the allegato property.
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
     * Gets the value of the dataAcquisizioneRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizioneRevoca() {
        return dataAcquisizioneRevoca;
    }

    /**
     * Sets the value of the dataAcquisizioneRevoca property.
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
     * Gets the value of the causaleRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return causaleRevoca;
    }

    /**
     * Sets the value of the causaleRevoca property.
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
     * Gets the value of the datiRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiRevoca() {
        return datiRevoca;
    }

    /**
     * Sets the value of the datiRevoca property.
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
     * Gets the value of the importoRevocato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoRevocato() {
        return importoRevocato;
    }

    /**
     * Sets the value of the importoRevocato property.
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
     * Gets the value of the esitoRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitoRevoca() {
        return esitoRevoca;
    }

    /**
     * Sets the value of the esitoRevoca property.
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
     * Gets the value of the datiEsitoRevoca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiEsitoRevoca() {
        return datiEsitoRevoca;
    }

    /**
     * Sets the value of the datiEsitoRevoca property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the tipo property.
         * 
         * @return
         *     possible object is
         *     {@link TipoAllegato }
         *     
         */
        public TipoAllegato getTipo() {
            return tipo;
        }

        /**
         * Sets the value of the tipo property.
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
         * Gets the value of the testo property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getTesto() {
            return testo;
        }

        /**
         * Sets the value of the testo property.
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
