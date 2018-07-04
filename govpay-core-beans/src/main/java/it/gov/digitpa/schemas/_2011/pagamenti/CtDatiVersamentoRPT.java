
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
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per ctDatiVersamentoRPT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctDatiVersamentoRPT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataEsecuzionePagamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stISODate"/&gt;
 *         &lt;element name="importoTotaleDaVersare" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stImporto"/&gt;
 *         &lt;element name="tipoVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stTipoVersamento"/&gt;
 *         &lt;element name="identificativoUnivocoVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="codiceContestoPagamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="ibanAddebito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stIBANIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="bicAddebito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stBICIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="firmaRicevuta" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stFirmaRicevuta"/&gt;
 *         &lt;element name="datiSingoloVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctDatiSingoloVersamentoRPT" maxOccurs="5"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctDatiVersamentoRPT", propOrder = {
    "dataEsecuzionePagamento",
    "importoTotaleDaVersare",
    "tipoVersamento",
    "identificativoUnivocoVersamento",
    "codiceContestoPagamento",
    "ibanAddebito",
    "bicAddebito",
    "firmaRicevuta",
    "datiSingoloVersamento"
})
public class CtDatiVersamentoRPT {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date dataEsecuzionePagamento;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoTotaleDaVersare;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StTipoVersamento tipoVersamento;
    @XmlElement(required = true)
    protected String identificativoUnivocoVersamento;
    @XmlElement(required = true)
    protected String codiceContestoPagamento;
    protected String ibanAddebito;
    protected String bicAddebito;
    @XmlElement(required = true)
    protected String firmaRicevuta;
    @XmlElement(required = true)
    protected List<CtDatiSingoloVersamentoRPT> datiSingoloVersamento;

    /**
     * Recupera il valore della proprietà dataEsecuzionePagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataEsecuzionePagamento() {
        return dataEsecuzionePagamento;
    }

    /**
     * Imposta il valore della proprietà dataEsecuzionePagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataEsecuzionePagamento(Date value) {
        this.dataEsecuzionePagamento = value;
    }

    /**
     * Recupera il valore della proprietà importoTotaleDaVersare.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoTotaleDaVersare() {
        return importoTotaleDaVersare;
    }

    /**
     * Imposta il valore della proprietà importoTotaleDaVersare.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoTotaleDaVersare(BigDecimal value) {
        this.importoTotaleDaVersare = value;
    }

    /**
     * Recupera il valore della proprietà tipoVersamento.
     * 
     * @return
     *     possible object is
     *     {@link StTipoVersamento }
     *     
     */
    public StTipoVersamento getTipoVersamento() {
        return tipoVersamento;
    }

    /**
     * Imposta il valore della proprietà tipoVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoVersamento }
     *     
     */
    public void setTipoVersamento(StTipoVersamento value) {
        this.tipoVersamento = value;
    }

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
     * Recupera il valore della proprietà codiceContestoPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceContestoPagamento() {
        return codiceContestoPagamento;
    }

    /**
     * Imposta il valore della proprietà codiceContestoPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceContestoPagamento(String value) {
        this.codiceContestoPagamento = value;
    }

    /**
     * Recupera il valore della proprietà ibanAddebito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanAddebito() {
        return ibanAddebito;
    }

    /**
     * Imposta il valore della proprietà ibanAddebito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanAddebito(String value) {
        this.ibanAddebito = value;
    }

    /**
     * Recupera il valore della proprietà bicAddebito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBicAddebito() {
        return bicAddebito;
    }

    /**
     * Imposta il valore della proprietà bicAddebito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBicAddebito(String value) {
        this.bicAddebito = value;
    }

    /**
     * Recupera il valore della proprietà firmaRicevuta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmaRicevuta() {
        return firmaRicevuta;
    }

    /**
     * Imposta il valore della proprietà firmaRicevuta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmaRicevuta(String value) {
        this.firmaRicevuta = value;
    }

    /**
     * Gets the value of the datiSingoloVersamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datiSingoloVersamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatiSingoloVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CtDatiSingoloVersamentoRPT }
     * 
     * 
     */
    public List<CtDatiSingoloVersamentoRPT> getDatiSingoloVersamento() {
        if (datiSingoloVersamento == null) {
            datiSingoloVersamento = new ArrayList<CtDatiSingoloVersamentoRPT>();
        }
        return this.datiSingoloVersamento;
    }

}
