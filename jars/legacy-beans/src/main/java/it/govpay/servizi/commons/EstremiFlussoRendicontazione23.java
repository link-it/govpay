
package it.govpay.servizi.commons;

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
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Java class for estremiFlussoRendicontazione_2.3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estremiFlussoRendicontazione_2.3"&gt;
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
 *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo18"/&gt;
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
@XmlType(name = "estremiFlussoRendicontazione_2.3", propOrder = {
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
    FlussoRendicontazione23 .class
})
public class EstremiFlussoRendicontazione23 {

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
     * Gets the value of the codFlusso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFlusso() {
        return codFlusso;
    }

    /**
     * Sets the value of the codFlusso property.
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
     * Gets the value of the dataFlusso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataFlusso() {
        return dataFlusso;
    }

    /**
     * Sets the value of the dataFlusso property.
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
     * Gets the value of the trn property.
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
     * Sets the value of the trn property.
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
     * Gets the value of the dataRegolamento property.
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
     * Sets the value of the dataRegolamento property.
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
     * Gets the value of the codPsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPsp() {
        return codPsp;
    }

    /**
     * Sets the value of the codPsp property.
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
     * Gets the value of the codBicRiversamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBicRiversamento() {
        return codBicRiversamento;
    }

    /**
     * Sets the value of the codBicRiversamento property.
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
     * Gets the value of the numeroPagamenti property.
     * 
     */
    public long getNumeroPagamenti() {
        return numeroPagamenti;
    }

    /**
     * Sets the value of the numeroPagamenti property.
     * 
     */
    public void setNumeroPagamenti(long value) {
        this.numeroPagamenti = value;
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
     * Gets the value of the stato property.
     * 
     * @return
     *     possible object is
     *     {@link StatoFr }
     *     
     */
    public StatoFr getStato() {
        return stato;
    }

    /**
     * Sets the value of the stato property.
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
        if (anomalia == null) {
            anomalia = new ArrayList<Anomalia>();
        }
        return this.anomalia;
    }

}
