
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
 * <p>Java class for estremiFlussoRendicontazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the annoRiferimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAnnoRiferimento() {
        return annoRiferimento;
    }

    /**
     * Sets the value of the annoRiferimento property.
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
     * Gets the value of the numeroPagamenti property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroPagamenti() {
        return numeroPagamenti;
    }

    /**
     * Sets the value of the numeroPagamenti property.
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

}
