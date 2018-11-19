
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
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per richiestaStorno complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="richiestaStorno"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codRichiesta" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="dataRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoRevoca"/&gt;
 *         &lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rr" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="er" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="importoStornato" type="{http://www.govpay.it/servizi/commons/}importo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "richiestaStorno", propOrder = {
    "codRichiesta",
    "dataRichiesta",
    "stato",
    "descrizioneStato",
    "rr",
    "er",
    "importoStornato"
})
public class RichiestaStorno {

    @XmlElement(required = true)
    protected String codRichiesta;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataRichiesta;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StatoRevoca stato;
    protected String descrizioneStato;
    @XmlElement(required = true)
    protected byte[] rr;
    protected byte[] er;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoStornato;

    /**
     * Recupera il valore della proprietà codRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRichiesta() {
        return this.codRichiesta;
    }

    /**
     * Imposta il valore della proprietà codRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRichiesta(String value) {
        this.codRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà dataRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataRichiesta() {
        return this.dataRichiesta;
    }

    /**
     * Imposta il valore della proprietà dataRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataRichiesta(Date value) {
        this.dataRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link StatoRevoca }
     *     
     */
    public StatoRevoca getStato() {
        return this.stato;
    }

    /**
     * Imposta il valore della proprietà stato.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoRevoca }
     *     
     */
    public void setStato(StatoRevoca value) {
        this.stato = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneStato() {
        return this.descrizioneStato;
    }

    /**
     * Imposta il valore della proprietà descrizioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneStato(String value) {
        this.descrizioneStato = value;
    }

    /**
     * Recupera il valore della proprietà rr.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRr() {
        return this.rr;
    }

    /**
     * Imposta il valore della proprietà rr.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRr(byte[] value) {
        this.rr = value;
    }

    /**
     * Recupera il valore della proprietà er.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEr() {
        return this.er;
    }

    /**
     * Imposta il valore della proprietà er.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEr(byte[] value) {
        this.er = value;
    }

    /**
     * Recupera il valore della proprietà importoStornato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoStornato() {
        return this.importoStornato;
    }

    /**
     * Imposta il valore della proprietà importoStornato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoStornato(BigDecimal value) {
        this.importoStornato = value;
    }

}
