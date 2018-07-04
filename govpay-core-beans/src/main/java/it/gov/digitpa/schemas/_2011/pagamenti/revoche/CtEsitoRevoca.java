
package it.gov.digitpa.schemas._2011.pagamenti.revoche;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;
import org.w3._2001.xmlschema.Adapter4;


/**
 * <p>Classe Java per ctEsitoRevoca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctEsitoRevoca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="versioneOggetto" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16"/&gt;
 *         &lt;element name="dominio" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctDominio"/&gt;
 *         &lt;element name="identificativoMessaggioEsito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35"/&gt;
 *         &lt;element name="dataOraMessaggioEsito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stISODateTime"/&gt;
 *         &lt;element name="riferimentoMessaggioRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35"/&gt;
 *         &lt;element name="riferimentoDataRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stISODate"/&gt;
 *         &lt;element name="istitutoAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctIstitutoAttestante"/&gt;
 *         &lt;element name="soggettoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctSoggettoVersante" minOccurs="0"/&gt;
 *         &lt;element name="soggettoPagatore" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctSoggettoPagatore"/&gt;
 *         &lt;element name="datiRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctDatiEsitoRevoca"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctEsitoRevoca", propOrder = {
    "versioneOggetto",
    "dominio",
    "identificativoMessaggioEsito",
    "dataOraMessaggioEsito",
    "riferimentoMessaggioRevoca",
    "riferimentoDataRevoca",
    "istitutoAttestante",
    "soggettoVersante",
    "soggettoPagatore",
    "datiRevoca"
})
public class CtEsitoRevoca {

    @XmlElement(required = true)
    protected String versioneOggetto;
    @XmlElement(required = true)
    protected CtDominio dominio;
    @XmlElement(required = true)
    protected String identificativoMessaggioEsito;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataOraMessaggioEsito;
    @XmlElement(required = true)
    protected String riferimentoMessaggioRevoca;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "date")
    protected Date riferimentoDataRevoca;
    @XmlElement(required = true)
    protected CtIstitutoAttestante istitutoAttestante;
    protected CtSoggettoVersante soggettoVersante;
    @XmlElement(required = true)
    protected CtSoggettoPagatore soggettoPagatore;
    @XmlElement(required = true)
    protected CtDatiEsitoRevoca datiRevoca;

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
     * Recupera il valore della proprietà identificativoMessaggioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioEsito() {
        return identificativoMessaggioEsito;
    }

    /**
     * Imposta il valore della proprietà identificativoMessaggioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioEsito(String value) {
        this.identificativoMessaggioEsito = value;
    }

    /**
     * Recupera il valore della proprietà dataOraMessaggioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataOraMessaggioEsito() {
        return dataOraMessaggioEsito;
    }

    /**
     * Imposta il valore della proprietà dataOraMessaggioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraMessaggioEsito(Date value) {
        this.dataOraMessaggioEsito = value;
    }

    /**
     * Recupera il valore della proprietà riferimentoMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiferimentoMessaggioRevoca() {
        return riferimentoMessaggioRevoca;
    }

    /**
     * Imposta il valore della proprietà riferimentoMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiferimentoMessaggioRevoca(String value) {
        this.riferimentoMessaggioRevoca = value;
    }

    /**
     * Recupera il valore della proprietà riferimentoDataRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getRiferimentoDataRevoca() {
        return riferimentoDataRevoca;
    }

    /**
     * Imposta il valore della proprietà riferimentoDataRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiferimentoDataRevoca(Date value) {
        this.riferimentoDataRevoca = value;
    }

    /**
     * Recupera il valore della proprietà istitutoAttestante.
     * 
     * @return
     *     possible object is
     *     {@link CtIstitutoAttestante }
     *     
     */
    public CtIstitutoAttestante getIstitutoAttestante() {
        return istitutoAttestante;
    }

    /**
     * Imposta il valore della proprietà istitutoAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIstitutoAttestante }
     *     
     */
    public void setIstitutoAttestante(CtIstitutoAttestante value) {
        this.istitutoAttestante = value;
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
     * Recupera il valore della proprietà datiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link CtDatiEsitoRevoca }
     *     
     */
    public CtDatiEsitoRevoca getDatiRevoca() {
        return datiRevoca;
    }

    /**
     * Imposta il valore della proprietà datiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link CtDatiEsitoRevoca }
     *     
     */
    public void setDatiRevoca(CtDatiEsitoRevoca value) {
        this.datiRevoca = value;
    }

}
