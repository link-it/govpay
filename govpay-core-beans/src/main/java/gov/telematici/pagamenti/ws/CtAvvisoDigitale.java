//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.07.10 alle 10:18:53 AM CEST 
//


package gov.telematici.pagamenti.ws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ctAvvisoDigitale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctAvvisoDigitale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificativoDominio" type="{http://ws.pagamenti.telematici.gov/}stText35"/>
 *         &lt;element name="anagraficaBeneficiario" type="{http://ws.pagamenti.telematici.gov/}stText35"/>
 *         &lt;element name="identificativoMessaggioRichiesta" type="{http://ws.pagamenti.telematici.gov/}stIdentificativoMessaggioRichiesta"/>
 *         &lt;element name="tassonomiaAvviso" type="{http://ws.pagamenti.telematici.gov/}stTassonomiaAvviso"/>
 *         &lt;element name="codiceAvviso" type="{http://ws.pagamenti.telematici.gov/}stCodiceAvviso"/>
 *         &lt;element name="soggettoPagatore" type="{http://ws.pagamenti.telematici.gov/}ctSoggettoPagatore"/>
 *         &lt;element name="dataScadenzaPagamento" type="{http://ws.pagamenti.telematici.gov/}stISODate"/>
 *         &lt;element name="dataScadenzaAvviso" type="{http://ws.pagamenti.telematici.gov/}stISODate"/>
 *         &lt;element name="importoAvviso" type="{http://ws.pagamenti.telematici.gov/}stImporto"/>
 *         &lt;element name="eMailSoggetto" type="{http://ws.pagamenti.telematici.gov/}stEMail" minOccurs="0"/>
 *         &lt;element name="cellulareSoggetto" type="{http://ws.pagamenti.telematici.gov/}stCellulareSoggetto" minOccurs="0"/>
 *         &lt;element name="descrizionePagamento" type="{http://ws.pagamenti.telematici.gov/}stText140"/>
 *         &lt;element name="urlAvviso" type="{http://ws.pagamenti.telematici.gov/}stText140" minOccurs="0"/>
 *         &lt;element name="datiSingoloVersamento" type="{http://ws.pagamenti.telematici.gov/}ctDatiSingoloVersamento" maxOccurs="5"/>
 *         &lt;element name="tipoPagamento" type="{http://ws.pagamenti.telematici.gov/}stTipoPagamento"/>
 *         &lt;element name="tipoOperazione" type="{http://ws.pagamenti.telematici.gov/}stTipoOperazione"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctAvvisoDigitale", propOrder = {
    "identificativoDominio",
    "anagraficaBeneficiario",
    "identificativoMessaggioRichiesta",
    "tassonomiaAvviso",
    "codiceAvviso",
    "soggettoPagatore",
    "dataScadenzaPagamento",
    "dataScadenzaAvviso",
    "importoAvviso",
    "eMailSoggetto",
    "cellulareSoggetto",
    "descrizionePagamento",
    "urlAvviso",
    "datiSingoloVersamento",
    "tipoPagamento",
    "tipoOperazione"
})
public class CtAvvisoDigitale {

    @XmlElement(required = true)
    protected String identificativoDominio;
    @XmlElement(required = true)
    protected String anagraficaBeneficiario;
    @XmlElement(required = true)
    protected String identificativoMessaggioRichiesta;
    @XmlElement(required = true)
    protected String tassonomiaAvviso;
    @XmlElement(required = true)
    protected String codiceAvviso;
    @XmlElement(required = true)
    protected CtSoggettoPagatore soggettoPagatore;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataScadenzaPagamento;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataScadenzaAvviso;
    @XmlElement(required = true)
    protected BigDecimal importoAvviso;
    protected String eMailSoggetto;
    protected String cellulareSoggetto;
    @XmlElement(required = true)
    protected String descrizionePagamento;
    protected String urlAvviso;
    @XmlElement(required = true)
    protected List<CtDatiSingoloVersamento> datiSingoloVersamento;
    @XmlElement(required = true)
    protected String tipoPagamento;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StTipoOperazione tipoOperazione;

    /**
     * Recupera il valore della proprietà identificativoDominio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoDominio() {
        return this.identificativoDominio;
    }

    /**
     * Imposta il valore della proprietà identificativoDominio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoDominio(String value) {
        this.identificativoDominio = value;
    }

    /**
     * Recupera il valore della proprietà anagraficaBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnagraficaBeneficiario() {
        return this.anagraficaBeneficiario;
    }

    /**
     * Imposta il valore della proprietà anagraficaBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnagraficaBeneficiario(String value) {
        this.anagraficaBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioRichiesta() {
        return this.identificativoMessaggioRichiesta;
    }

    /**
     * Imposta il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioRichiesta(String value) {
        this.identificativoMessaggioRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà tassonomiaAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTassonomiaAvviso() {
        return this.tassonomiaAvviso;
    }

    /**
     * Imposta il valore della proprietà tassonomiaAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTassonomiaAvviso(String value) {
        this.tassonomiaAvviso = value;
    }

    /**
     * Recupera il valore della proprietà codiceAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAvviso() {
        return this.codiceAvviso;
    }

    /**
     * Imposta il valore della proprietà codiceAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAvviso(String value) {
        this.codiceAvviso = value;
    }

    /**
     * Recupera il valore della proprietà soggettoPagatore.
     * 
     * @return
     *     possible object is
     *     {@link CtSoggettoPagatore }
     *     
     */
    public CtSoggettoPagatore getSoggettoPagatore() {
        return this.soggettoPagatore;
    }

    /**
     * Imposta il valore della proprietà soggettoPagatore.
     * 
     * @param value
     *     allowed object is
     *     {@link CtSoggettoPagatore }
     *     
     */
    public void setSoggettoPagatore(CtSoggettoPagatore value) {
        this.soggettoPagatore = value;
    }

    /**
     * Recupera il valore della proprietà dataScadenzaPagamento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataScadenzaPagamento() {
        return this.dataScadenzaPagamento;
    }

    /**
     * Imposta il valore della proprietà dataScadenzaPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataScadenzaPagamento(XMLGregorianCalendar value) {
        this.dataScadenzaPagamento = value;
    }

    /**
     * Recupera il valore della proprietà dataScadenzaAvviso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataScadenzaAvviso() {
        return this.dataScadenzaAvviso;
    }

    /**
     * Imposta il valore della proprietà dataScadenzaAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataScadenzaAvviso(XMLGregorianCalendar value) {
        this.dataScadenzaAvviso = value;
    }

    /**
     * Recupera il valore della proprietà importoAvviso.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoAvviso() {
        return this.importoAvviso;
    }

    /**
     * Imposta il valore della proprietà importoAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoAvviso(BigDecimal value) {
        this.importoAvviso = value;
    }

    /**
     * Recupera il valore della proprietà eMailSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMailSoggetto() {
        return this.eMailSoggetto;
    }

    /**
     * Imposta il valore della proprietà eMailSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMailSoggetto(String value) {
        this.eMailSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà cellulareSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellulareSoggetto() {
        return this.cellulareSoggetto;
    }

    /**
     * Imposta il valore della proprietà cellulareSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellulareSoggetto(String value) {
        this.cellulareSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà descrizionePagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizionePagamento() {
        return this.descrizionePagamento;
    }

    /**
     * Imposta il valore della proprietà descrizionePagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizionePagamento(String value) {
        this.descrizionePagamento = value;
    }

    /**
     * Recupera il valore della proprietà urlAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlAvviso() {
        return this.urlAvviso;
    }

    /**
     * Imposta il valore della proprietà urlAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlAvviso(String value) {
        this.urlAvviso = value;
    }

    /**
     * Gets the value of the datiSingoloVersamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datiSingoloVersamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatiSingoloVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CtDatiSingoloVersamento }
     * 
     * 
     */
    public List<CtDatiSingoloVersamento> getDatiSingoloVersamento() {
        if (this.datiSingoloVersamento == null) {
            this.datiSingoloVersamento = new ArrayList<>();
        }
        return this.datiSingoloVersamento;
    }

    /**
     * Recupera il valore della proprietà tipoPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPagamento() {
        return this.tipoPagamento;
    }

    /**
     * Imposta il valore della proprietà tipoPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPagamento(String value) {
        this.tipoPagamento = value;
    }

    /**
     * Recupera il valore della proprietà tipoOperazione.
     * 
     * @return
     *     possible object is
     *     {@link StTipoOperazione }
     *     
     */
    public StTipoOperazione getTipoOperazione() {
        return this.tipoOperazione;
    }

    /**
     * Imposta il valore della proprietà tipoOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoOperazione }
     *     
     */
    public void setTipoOperazione(StTipoOperazione value) {
        this.tipoOperazione = value;
    }

}
