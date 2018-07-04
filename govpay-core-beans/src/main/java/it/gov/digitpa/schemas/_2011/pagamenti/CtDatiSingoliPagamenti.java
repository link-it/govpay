
package it.gov.digitpa.schemas._2011.pagamenti;

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
 * <p>Classe Java per ctDatiSingoliPagamenti complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctDatiSingoliPagamenti"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="identificativoUnivocoRiscossione" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="indiceDatiSingoloPagamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stIndice" minOccurs="0"/&gt;
 *         &lt;element name="singoloImportoPagato" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stImporto"/&gt;
 *         &lt;element name="codiceEsitoSingoloPagamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stCodiceEsitoPagamento"/&gt;
 *         &lt;element name="dataEsitoSingoloPagamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stISODate"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctDatiSingoliPagamenti", propOrder = {
    "identificativoUnivocoVersamento",
    "identificativoUnivocoRiscossione",
    "indiceDatiSingoloPagamento",
    "singoloImportoPagato",
    "codiceEsitoSingoloPagamento",
    "dataEsitoSingoloPagamento"
})
public class CtDatiSingoliPagamenti {

    @XmlElement(required = true)
    protected String identificativoUnivocoVersamento;
    @XmlElement(required = true)
    protected String identificativoUnivocoRiscossione;
    @XmlSchemaType(name = "integer")
    protected Integer indiceDatiSingoloPagamento;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal singoloImportoPagato;
    @XmlElement(required = true)
    protected String codiceEsitoSingoloPagamento;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataEsitoSingoloPagamento;

    /**
     * Recupera il valore della proprietà identificativoUnivocoVersamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoUnivocoVersamento() {
        return identificativoUnivocoVersamento;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoUnivocoVersamento(String value) {
        this.identificativoUnivocoVersamento = value;
    }

    /**
     * Recupera il valore della proprietà identificativoUnivocoRiscossione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoUnivocoRiscossione() {
        return identificativoUnivocoRiscossione;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoRiscossione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoUnivocoRiscossione(String value) {
        this.identificativoUnivocoRiscossione = value;
    }

    /**
     * Recupera il valore della proprietà indiceDatiSingoloPagamento.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIndiceDatiSingoloPagamento() {
        return indiceDatiSingoloPagamento;
    }

    /**
     * Imposta il valore della proprietà indiceDatiSingoloPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIndiceDatiSingoloPagamento(Integer value) {
        this.indiceDatiSingoloPagamento = value;
    }

    /**
     * Recupera il valore della proprietà singoloImportoPagato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getSingoloImportoPagato() {
        return singoloImportoPagato;
    }

    /**
     * Imposta il valore della proprietà singoloImportoPagato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSingoloImportoPagato(BigDecimal value) {
        this.singoloImportoPagato = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsitoSingoloPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEsitoSingoloPagamento() {
        return codiceEsitoSingoloPagamento;
    }

    /**
     * Imposta il valore della proprietà codiceEsitoSingoloPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEsitoSingoloPagamento(String value) {
        this.codiceEsitoSingoloPagamento = value;
    }

    /**
     * Recupera il valore della proprietà dataEsitoSingoloPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataEsitoSingoloPagamento() {
        return dataEsitoSingoloPagamento;
    }

    /**
     * Imposta il valore della proprietà dataEsitoSingoloPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataEsitoSingoloPagamento(Date value) {
        this.dataEsitoSingoloPagamento = value;
    }

}
