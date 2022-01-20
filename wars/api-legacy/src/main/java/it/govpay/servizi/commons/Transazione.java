
package it.govpay.servizi.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Java class for transazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="modello" type="{http://www.govpay.it/servizi/commons/}modelloPagamento"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="canale" type="{http://www.govpay.it/servizi/commons/}canale" minOccurs="0"/&gt;
 *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoTransazione"/&gt;
 *         &lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="esito" type="{http://www.govpay.it/servizi/commons/}esitoTransazione" minOccurs="0"/&gt;
 *         &lt;element name="rpt" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="rt" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="richiestaStorno" type="{http://www.govpay.it/servizi/commons/}richiestaStorno" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="pagamento" type="{http://www.govpay.it/servizi/commons/}pagamento" maxOccurs="5" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transazione", propOrder = {
    "data",
    "modello",
    "codDominio",
    "iuv",
    "ccp",
    "canale",
    "stato",
    "descrizioneStato",
    "esito",
    "rpt",
    "rt",
    "richiestaStorno",
    "pagamento"
})
public class Transazione {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date data;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ModelloPagamento modello;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String iuv;
    @XmlElement(required = true)
    protected String ccp;
    protected Canale canale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StatoTransazione stato;
    protected String descrizioneStato;
    @XmlSchemaType(name = "string")
    protected EsitoTransazione esito;
    @XmlElement(required = true)
    protected byte[] rpt;
    protected byte[] rt;
    protected List<RichiestaStorno> richiestaStorno;
    protected List<Pagamento> pagamento;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(Date value) {
        this.data = value;
    }

    /**
     * Gets the value of the modello property.
     * 
     * @return
     *     possible object is
     *     {@link ModelloPagamento }
     *     
     */
    public ModelloPagamento getModello() {
        return modello;
    }

    /**
     * Sets the value of the modello property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelloPagamento }
     *     
     */
    public void setModello(ModelloPagamento value) {
        this.modello = value;
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
     * Gets the value of the iuv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIuv() {
        return iuv;
    }

    /**
     * Sets the value of the iuv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIuv(String value) {
        this.iuv = value;
    }

    /**
     * Gets the value of the ccp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcp() {
        return ccp;
    }

    /**
     * Sets the value of the ccp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcp(String value) {
        this.ccp = value;
    }

    /**
     * Gets the value of the canale property.
     * 
     * @return
     *     possible object is
     *     {@link Canale }
     *     
     */
    public Canale getCanale() {
        return canale;
    }

    /**
     * Sets the value of the canale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Canale }
     *     
     */
    public void setCanale(Canale value) {
        this.canale = value;
    }

    /**
     * Gets the value of the stato property.
     * 
     * @return
     *     possible object is
     *     {@link StatoTransazione }
     *     
     */
    public StatoTransazione getStato() {
        return stato;
    }

    /**
     * Sets the value of the stato property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatoTransazione }
     *     
     */
    public void setStato(StatoTransazione value) {
        this.stato = value;
    }

    /**
     * Gets the value of the descrizioneStato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneStato() {
        return descrizioneStato;
    }

    /**
     * Sets the value of the descrizioneStato property.
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
     * Gets the value of the esito property.
     * 
     * @return
     *     possible object is
     *     {@link EsitoTransazione }
     *     
     */
    public EsitoTransazione getEsito() {
        return esito;
    }

    /**
     * Sets the value of the esito property.
     * 
     * @param value
     *     allowed object is
     *     {@link EsitoTransazione }
     *     
     */
    public void setEsito(EsitoTransazione value) {
        this.esito = value;
    }

    /**
     * Gets the value of the rpt property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRpt() {
        return rpt;
    }

    /**
     * Sets the value of the rpt property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRpt(byte[] value) {
        this.rpt = value;
    }

    /**
     * Gets the value of the rt property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRt() {
        return rt;
    }

    /**
     * Sets the value of the rt property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRt(byte[] value) {
        this.rt = value;
    }

    /**
     * Gets the value of the richiestaStorno property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the richiestaStorno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRichiestaStorno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RichiestaStorno }
     * 
     * 
     */
    public List<RichiestaStorno> getRichiestaStorno() {
        if (richiestaStorno == null) {
            richiestaStorno = new ArrayList<RichiestaStorno>();
        }
        return this.richiestaStorno;
    }

    /**
     * Gets the value of the pagamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pagamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPagamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pagamento }
     * 
     * 
     */
    public List<Pagamento> getPagamento() {
        if (pagamento == null) {
            pagamento = new ArrayList<Pagamento>();
        }
        return this.pagamento;
    }

}
