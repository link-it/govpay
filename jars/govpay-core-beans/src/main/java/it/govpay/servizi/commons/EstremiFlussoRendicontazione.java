
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.Date;
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
import org.w3._2001.xmlschema.Adapter5;


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
 *         &lt;element name="codBicRiversamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}gYear"/&gt;
 *         &lt;element name="codPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="dataFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}string35"/&gt;
 *         &lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo18" minOccurs="0"/&gt;
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
    "codBicRiversamento",
    "annoRiferimento",
    "codPsp",
    "dataFlusso",
    "dataRegolamento",
    "iur",
    "numeroPagamenti",
    "importoTotale"
})
@XmlSeeAlso({
    FlussoRendicontazione.class
})
public class EstremiFlussoRendicontazione {

    @XmlElement(required = true)
    protected String codFlusso;
    protected String codBicRiversamento;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter5 .class)
    @XmlSchemaType(name = "gYear")
    protected Integer annoRiferimento;
    @XmlElement(required = true)
    protected String codPsp;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataFlusso;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataRegolamento;
    @XmlElement(required = true)
    protected String iur;
    protected Long numeroPagamenti;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotale;

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
     * Recupera il valore della proprietà annoRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAnnoRiferimento() {
        return this.annoRiferimento;
    }

    /**
     * Imposta il valore della proprietà annoRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoRiferimento(Integer value) {
        this.annoRiferimento = value;
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
     * Recupera il valore della proprietà numeroPagamenti.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroPagamenti() {
        return this.numeroPagamenti;
    }

    /**
     * Imposta il valore della proprietà numeroPagamenti.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumeroPagamenti(Long value) {
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

}
