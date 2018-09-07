
package it.govpay.servizi.v2_3.gprnd;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter4;


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
 *         &lt;element name="trn" type="{http://www.govpay.it/servizi/commons/}string512"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string512"/&gt;
 *         &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
 *         &lt;element name="dataValuta" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dataContabile" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dispositivo" type="{http://www.govpay.it/servizi/commons/}string256" minOccurs="0"/&gt;
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
    "trn",
    "codDominio",
    "causale",
    "importo",
    "dataValuta",
    "dataContabile",
    "dispositivo"
})
@XmlRootElement(name = "gpRegistraIncasso")
public class GpRegistraIncasso {

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
     * Recupera il valore della proprietà causale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausale() {
        return this.causale;
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
        return this.importo;
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
        return this.dataValuta;
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
        return this.dataContabile;
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
        return this.dispositivo;
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

}
