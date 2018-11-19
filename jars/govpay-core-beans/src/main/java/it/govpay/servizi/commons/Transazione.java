
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
 * <p>Classe Java per transazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getData() {
        return this.data;
    }

    /**
     * Imposta il valore della proprietà data.
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
     * Recupera il valore della proprietà modello.
     * 
     * @return
     *     possible object is
     *     {@link ModelloPagamento }
     *     
     */
    public ModelloPagamento getModello() {
        return this.modello;
    }

    /**
     * Imposta il valore della proprietà modello.
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
     * Recupera il valore della proprietà iuv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIuv() {
        return this.iuv;
    }

    /**
     * Imposta il valore della proprietà iuv.
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
     * Recupera il valore della proprietà ccp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcp() {
        return this.ccp;
    }

    /**
     * Imposta il valore della proprietà ccp.
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
     * Recupera il valore della proprietà canale.
     * 
     * @return
     *     possible object is
     *     {@link Canale }
     *     
     */
    public Canale getCanale() {
        return this.canale;
    }

    /**
     * Imposta il valore della proprietà canale.
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
     * Recupera il valore della proprietà stato.
     * 
     * @return
     *     possible object is
     *     {@link StatoTransazione }
     *     
     */
    public StatoTransazione getStato() {
        return this.stato;
    }

    /**
     * Imposta il valore della proprietà stato.
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
     * Recupera il valore della proprietà esito.
     * 
     * @return
     *     possible object is
     *     {@link EsitoTransazione }
     *     
     */
    public EsitoTransazione getEsito() {
        return this.esito;
    }

    /**
     * Imposta il valore della proprietà esito.
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
     * Recupera il valore della proprietà rpt.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRpt() {
        return this.rpt;
    }

    /**
     * Imposta il valore della proprietà rpt.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRpt(byte[] value) {
        this.rpt = value;
    }

    /**
     * Recupera il valore della proprietà rt.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRt() {
        return this.rt;
    }

    /**
     * Imposta il valore della proprietà rt.
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
        if (this.richiestaStorno == null) {
            this.richiestaStorno = new ArrayList<>();
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
        if (this.pagamento == null) {
            this.pagamento = new ArrayList<>();
        }
        return this.pagamento;
    }

}
