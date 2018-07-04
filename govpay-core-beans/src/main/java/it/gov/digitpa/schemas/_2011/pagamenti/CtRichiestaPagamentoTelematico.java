
package it.gov.digitpa.schemas._2011.pagamenti;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per ctRichiestaPagamentoTelematico complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctRichiestaPagamentoTelematico"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="versioneOggetto" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText16"/&gt;
 *         &lt;element name="dominio" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctDominio"/&gt;
 *         &lt;element name="identificativoMessaggioRichiesta" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *         &lt;element name="dataOraMessaggioRichiesta" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stISODateTime"/&gt;
 *         &lt;element name="autenticazioneSoggetto" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stAutenticazioneSoggetto"/&gt;
 *         &lt;element name="soggettoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctSoggettoVersante" minOccurs="0"/&gt;
 *         &lt;element name="soggettoPagatore" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctSoggettoPagatore"/&gt;
 *         &lt;element name="enteBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctEnteBeneficiario"/&gt;
 *         &lt;element name="datiVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctDatiVersamentoRPT"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctRichiestaPagamentoTelematico", propOrder = {
    "versioneOggetto",
    "dominio",
    "identificativoMessaggioRichiesta",
    "dataOraMessaggioRichiesta",
    "autenticazioneSoggetto",
    "soggettoVersante",
    "soggettoPagatore",
    "enteBeneficiario",
    "datiVersamento"
})
public class CtRichiestaPagamentoTelematico {

    @XmlElement(required = true)
    protected String versioneOggetto;
    @XmlElement(required = true)
    protected CtDominio dominio;
    @XmlElement(required = true)
    protected String identificativoMessaggioRichiesta;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataOraMessaggioRichiesta;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StAutenticazioneSoggetto autenticazioneSoggetto;
    protected CtSoggettoVersante soggettoVersante;
    @XmlElement(required = true)
    protected CtSoggettoPagatore soggettoPagatore;
    @XmlElement(required = true)
    protected CtEnteBeneficiario enteBeneficiario;
    @XmlElement(required = true)
    protected CtDatiVersamentoRPT datiVersamento;

    /**
     * Recupera il valore della proprietà versioneOggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersioneOggetto() {
        return versioneOggetto;
    }

    /**
     * Imposta il valore della proprietà versioneOggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersioneOggetto(String value) {
        this.versioneOggetto = value;
    }

    /**
     * Recupera il valore della proprietà dominio.
     * 
     * @return
     *     possible object is
     *     {@link CtDominio }
     *     
     */
    public CtDominio getDominio() {
        return dominio;
    }

    /**
     * Imposta il valore della proprietà dominio.
     * 
     * @param value
     *     allowed object is
     *     {@link CtDominio }
     *     
     */
    public void setDominio(CtDominio value) {
        this.dominio = value;
    }

    /**
     * Recupera il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioRichiesta() {
        return identificativoMessaggioRichiesta;
    }

    /**
     * Imposta il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioRichiesta(String value) {
        this.identificativoMessaggioRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà dataOraMessaggioRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataOraMessaggioRichiesta() {
        return dataOraMessaggioRichiesta;
    }

    /**
     * Imposta il valore della proprietà dataOraMessaggioRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraMessaggioRichiesta(Date value) {
        this.dataOraMessaggioRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà autenticazioneSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link StAutenticazioneSoggetto }
     *     
     */
    public StAutenticazioneSoggetto getAutenticazioneSoggetto() {
        return autenticazioneSoggetto;
    }

    /**
     * Imposta il valore della proprietà autenticazioneSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link StAutenticazioneSoggetto }
     *     
     */
    public void setAutenticazioneSoggetto(StAutenticazioneSoggetto value) {
        this.autenticazioneSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà soggettoVersante.
     * 
     * @return
     *     possible object is
     *     {@link CtSoggettoVersante }
     *     
     */
    public CtSoggettoVersante getSoggettoVersante() {
        return soggettoVersante;
    }

    /**
     * Imposta il valore della proprietà soggettoVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link CtSoggettoVersante }
     *     
     */
    public void setSoggettoVersante(CtSoggettoVersante value) {
        this.soggettoVersante = value;
    }

    /**
     * Recupera il valore della proprietà soggettoPagatore.
     * 
     * @return
     *     possible object is
     *     {@link CtSoggettoPagatore }
     *     
     */
    public CtSoggettoPagatore getSoggettoPagatore() {
        return soggettoPagatore;
    }

    /**
     * Imposta il valore della proprietà soggettoPagatore.
     * 
     * @param value
     *     allowed object is
     *     {@link CtSoggettoPagatore }
     *     
     */
    public void setSoggettoPagatore(CtSoggettoPagatore value) {
        this.soggettoPagatore = value;
    }

    /**
     * Recupera il valore della proprietà enteBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link CtEnteBeneficiario }
     *     
     */
    public CtEnteBeneficiario getEnteBeneficiario() {
        return enteBeneficiario;
    }

    /**
     * Imposta il valore della proprietà enteBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link CtEnteBeneficiario }
     *     
     */
    public void setEnteBeneficiario(CtEnteBeneficiario value) {
        this.enteBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà datiVersamento.
     * 
     * @return
     *     possible object is
     *     {@link CtDatiVersamentoRPT }
     *     
     */
    public CtDatiVersamentoRPT getDatiVersamento() {
        return datiVersamento;
    }

    /**
     * Imposta il valore della proprietà datiVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link CtDatiVersamentoRPT }
     *     
     */
    public void setDatiVersamento(CtDatiVersamentoRPT value) {
        this.datiVersamento = value;
    }

}
