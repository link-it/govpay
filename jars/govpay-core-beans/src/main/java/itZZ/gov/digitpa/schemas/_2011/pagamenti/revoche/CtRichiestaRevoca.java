
package itZZ.gov.digitpa.schemas._2011.pagamenti.revoche;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per ctRichiestaRevoca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctRichiestaRevoca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="versioneOggetto" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16"/&gt;
 *         &lt;element name="dominio" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctDominio"/&gt;
 *         &lt;element name="identificativoMessaggioRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35"/&gt;
 *         &lt;element name="dataOraMessaggioRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stISODateTime"/&gt;
 *         &lt;element name="istitutoAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctIstitutoAttestante"/&gt;
 *         &lt;element name="soggettoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctSoggettoVersante" minOccurs="0"/&gt;
 *         &lt;element name="soggettoPagatore" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctSoggettoPagatore"/&gt;
 *         &lt;element name="datiRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctDatiRevoca"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctRichiestaRevoca", propOrder = {
    "versioneOggetto",
    "dominio",
    "identificativoMessaggioRevoca",
    "dataOraMessaggioRevoca",
    "istitutoAttestante",
    "soggettoVersante",
    "soggettoPagatore",
    "datiRevoca"
})
public class CtRichiestaRevoca {

    @XmlElement(required = true)
    protected String versioneOggetto;
    @XmlElement(required = true)
    protected CtDominio dominio;
    @XmlElement(required = true)
    protected String identificativoMessaggioRevoca;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataOraMessaggioRevoca;
    @XmlElement(required = true)
    protected CtIstitutoAttestante istitutoAttestante;
    protected CtSoggettoVersante soggettoVersante;
    @XmlElement(required = true)
    protected CtSoggettoPagatore soggettoPagatore;
    @XmlElement(required = true)
    protected CtDatiRevoca datiRevoca;

    /**
     * Recupera il valore della proprietà versioneOggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersioneOggetto() {
        return this.versioneOggetto;
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
        return this.dominio;
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
     * Recupera il valore della proprietà identificativoMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioRevoca() {
        return this.identificativoMessaggioRevoca;
    }

    /**
     * Imposta il valore della proprietà identificativoMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioRevoca(String value) {
        this.identificativoMessaggioRevoca = value;
    }

    /**
     * Recupera il valore della proprietà dataOraMessaggioRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataOraMessaggioRevoca() {
        return this.dataOraMessaggioRevoca;
    }

    /**
     * Imposta il valore della proprietà dataOraMessaggioRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraMessaggioRevoca(Date value) {
        this.dataOraMessaggioRevoca = value;
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
        return this.istitutoAttestante;
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
        return this.soggettoVersante;
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
        return this.soggettoPagatore;
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
     *     {@link CtDatiRevoca }
     *     
     */
    public CtDatiRevoca getDatiRevoca() {
        return this.datiRevoca;
    }

    /**
     * Imposta il valore della proprietà datiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link CtDatiRevoca }
     *     
     */
    public void setDatiRevoca(CtDatiRevoca value) {
        this.datiRevoca = value;
    }

}
