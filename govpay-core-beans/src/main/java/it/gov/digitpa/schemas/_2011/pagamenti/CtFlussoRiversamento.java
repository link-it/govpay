
package it.gov.digitpa.schemas._2011.pagamenti;

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
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per ctFlussoRiversamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctFlussoRiversamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="versioneOggetto" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stVersioneOggetto"/&gt;
 *         &lt;element name="identificativoFlusso" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="dataOraFlusso" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stISODateTime"/&gt;
 *         &lt;element name="identificativoUnivocoRegolamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="dataRegolamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stISODate"/&gt;
 *         &lt;element name="istitutoMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIstitutoMittente"/&gt;
 *         &lt;element name="codiceBicBancaDiRiversamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="istitutoRicevente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIstitutoRicevente"/&gt;
 *         &lt;element name="numeroTotalePagamenti" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stNumeroTotalePagamenti"/&gt;
 *         &lt;element name="importoTotalePagamenti" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stImportoTotalePagamenti"/&gt;
 *         &lt;element name="datiSingoliPagamenti" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctDatiSingoliPagamenti" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctFlussoRiversamento", propOrder = {
    "versioneOggetto",
    "identificativoFlusso",
    "dataOraFlusso",
    "identificativoUnivocoRegolamento",
    "dataRegolamento",
    "istitutoMittente",
    "codiceBicBancaDiRiversamento",
    "istitutoRicevente",
    "numeroTotalePagamenti",
    "importoTotalePagamenti",
    "datiSingoliPagamenti"
})
public class CtFlussoRiversamento {

    @XmlElement(required = true)
    protected String versioneOggetto;
    @XmlElement(required = true)
    protected String identificativoFlusso;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataOraFlusso;
    @XmlElement(required = true)
    protected String identificativoUnivocoRegolamento;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataRegolamento;
    @XmlElement(required = true)
    protected CtIstitutoMittente istitutoMittente;
    protected String codiceBicBancaDiRiversamento;
    @XmlElement(required = true)
    protected CtIstitutoRicevente istitutoRicevente;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal numeroTotalePagamenti;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotalePagamenti;
    @XmlElement(required = true)
    protected List<CtDatiSingoliPagamenti> datiSingoliPagamenti;

    /**
     * Recupera il valore della proprietà versioneOggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersioneOggetto() {
        return versioneOggetto;
    }

    /**
     * Imposta il valore della proprietà versioneOggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersioneOggetto(String value) {
        this.versioneOggetto = value;
    }

    /**
     * Recupera il valore della proprietà identificativoFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoFlusso() {
        return identificativoFlusso;
    }

    /**
     * Imposta il valore della proprietà identificativoFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoFlusso(String value) {
        this.identificativoFlusso = value;
    }

    /**
     * Recupera il valore della proprietà dataOraFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataOraFlusso() {
        return dataOraFlusso;
    }

    /**
     * Imposta il valore della proprietà dataOraFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraFlusso(Date value) {
        this.dataOraFlusso = value;
    }

    /**
     * Recupera il valore della proprietà identificativoUnivocoRegolamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoUnivocoRegolamento() {
        return identificativoUnivocoRegolamento;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoRegolamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoUnivocoRegolamento(String value) {
        this.identificativoUnivocoRegolamento = value;
    }

    /**
     * Recupera il valore della proprietà dataRegolamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataRegolamento() {
        return dataRegolamento;
    }

    /**
     * Imposta il valore della proprietà dataRegolamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataRegolamento(Date value) {
        this.dataRegolamento = value;
    }

    /**
     * Recupera il valore della proprietà istitutoMittente.
     * 
     * @return
     *     possible object is
     *     {@link CtIstitutoMittente }
     *     
     */
    public CtIstitutoMittente getIstitutoMittente() {
        return istitutoMittente;
    }

    /**
     * Imposta il valore della proprietà istitutoMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIstitutoMittente }
     *     
     */
    public void setIstitutoMittente(CtIstitutoMittente value) {
        this.istitutoMittente = value;
    }

    /**
     * Recupera il valore della proprietà codiceBicBancaDiRiversamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceBicBancaDiRiversamento() {
        return codiceBicBancaDiRiversamento;
    }

    /**
     * Imposta il valore della proprietà codiceBicBancaDiRiversamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceBicBancaDiRiversamento(String value) {
        this.codiceBicBancaDiRiversamento = value;
    }

    /**
     * Recupera il valore della proprietà istitutoRicevente.
     * 
     * @return
     *     possible object is
     *     {@link CtIstitutoRicevente }
     *     
     */
    public CtIstitutoRicevente getIstitutoRicevente() {
        return istitutoRicevente;
    }

    /**
     * Imposta il valore della proprietà istitutoRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIstitutoRicevente }
     *     
     */
    public void setIstitutoRicevente(CtIstitutoRicevente value) {
        this.istitutoRicevente = value;
    }

    /**
     * Recupera il valore della proprietà numeroTotalePagamenti.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getNumeroTotalePagamenti() {
        return numeroTotalePagamenti;
    }

    /**
     * Imposta il valore della proprietà numeroTotalePagamenti.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTotalePagamenti(BigDecimal value) {
        this.numeroTotalePagamenti = value;
    }

    /**
     * Recupera il valore della proprietà importoTotalePagamenti.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoTotalePagamenti() {
        return importoTotalePagamenti;
    }

    /**
     * Imposta il valore della proprietà importoTotalePagamenti.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoTotalePagamenti(BigDecimal value) {
        this.importoTotalePagamenti = value;
    }

    /**
     * Gets the value of the datiSingoliPagamenti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datiSingoliPagamenti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatiSingoliPagamenti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CtDatiSingoliPagamenti }
     * 
     * 
     */
    public List<CtDatiSingoliPagamenti> getDatiSingoliPagamenti() {
        if (datiSingoliPagamenti == null) {
            datiSingoliPagamenti = new ArrayList<CtDatiSingoliPagamenti>();
        }
        return this.datiSingoliPagamenti;
    }

}
