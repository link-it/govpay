
package it.govpay.servizi.v2_3.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import it.govpay.servizi.commons.Anomalia;
import it.govpay.servizi.commons.StatoFr;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per estremiFlussoRendicontazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="estremiFlussoRendicontazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codFlusso" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="dataFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="trn" type="{http://www.govpay.it/servizi/commons/}string35"/&gt;
 *         &lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="codPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codBicRiversamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/v2_3/commons/}importo18_rnd"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoFr"/&gt;
 *         &lt;element name="anomalia" type="{http://www.govpay.it/servizi/commons/}anomalia" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estremiFlussoRendicontazione", propOrder = {
    "codFlusso",
    "dataFlusso",
    "trn",
    "dataRegolamento",
    "codPsp",
    "codBicRiversamento",
    "codDominio",
    "numeroPagamenti",
    "importoTotale",
    "stato",
    "anomalia"
})
@XmlSeeAlso({
    FlussoRendicontazione.class
})
public class EstremiFlussoRendicontazione {

    @XmlElement(required = true)
    protected String codFlusso;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataFlusso;
    @XmlElement(required = true)
    protected String trn;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataRegolamento;
    @XmlElement(required = true)
    protected String codPsp;
    protected String codBicRiversamento;
    @XmlElement(required = true)
    protected String codDominio;
    protected long numeroPagamenti;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StatoFr stato;
    protected List<Anomalia> anomalia;

    /**
     * Recupera il valore della proprietà codFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFlusso() {
        return this.codFlusso;
    }

    /**
     * Imposta il valore della proprietà codFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFlusso(String value) {
        this.codFlusso = value;
    }

    /**
     * Recupera il valore della proprietà dataFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataFlusso() {
        return this.dataFlusso;
    }

    /**
     * Imposta il valore della proprietà dataFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFlusso(Date value) {
        this.dataFlusso = value;
    }

    /**
     * Recupera il valore della proprietà trn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrn() {
        return this.trn;
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
     * Recupera il valore della proprietà dataRegolamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataRegolamento() {
        return this.dataRegolamento;
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
     * Recupera il valore della proprietà codPsp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPsp() {
        return this.codPsp;
    }

    /**
     * Imposta il valore della proprietà codPsp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPsp(String value) {
        this.codPsp = value;
    }

    /**
     * Recupera il valore della proprietà codBicRiversamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBicRiversamento() {
        return this.codBicRiversamento;
    }

    /**
     * Imposta il valore della proprietà codBicRiversamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBicRiversamento(String value) {
        this.codBicRiversamento = value;
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
        return this.codDominio;
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
     * Recupera il valore della proprietà numeroPagamenti.
     * 
     */
    public long getNumeroPagamenti() {
        return this.numeroPagamenti;
    }

    /**
     * Imposta il valore della proprietà numeroPagamenti.
     * 
     */
    public void setNumeroPagamenti(long value) {
        this.numeroPagamenti = value;
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
        return this.importoTotale;
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
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link StatoFr }
     *     
     */
    public StatoFr getStato() {
        return this.stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoFr }
     *     
     */
    public void setStato(StatoFr value) {
        this.stato = value;
    }

    /**
     * Gets the value of the anomalia property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anomalia property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnomalia().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Anomalia }
     * 
     * 
     */
    public List<Anomalia> getAnomalia() {
        if (this.anomalia == null) {
            this.anomalia = new ArrayList<>();
        }
        return this.anomalia;
    }

}
