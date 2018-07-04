
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctIstitutoAttestante complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctIstitutoAttestante"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIdentificativoUnivoco"/&gt;
 *         &lt;element name="denominazioneAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70"/&gt;
 *         &lt;element name="codiceUnitOperAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="denomUnitOperAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="indirizzoAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="civicoAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="capAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="localitaAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="provinciaAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="nazioneAttestante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stNazioneProvincia" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctIstitutoAttestante", propOrder = {
    "identificativoUnivocoAttestante",
    "denominazioneAttestante",
    "codiceUnitOperAttestante",
    "denomUnitOperAttestante",
    "indirizzoAttestante",
    "civicoAttestante",
    "capAttestante",
    "localitaAttestante",
    "provinciaAttestante",
    "nazioneAttestante"
})
public class CtIstitutoAttestante {

    @XmlElement(required = true)
    protected CtIdentificativoUnivoco identificativoUnivocoAttestante;
    @XmlElement(required = true)
    protected String denominazioneAttestante;
    protected String codiceUnitOperAttestante;
    protected String denomUnitOperAttestante;
    protected String indirizzoAttestante;
    protected String civicoAttestante;
    protected String capAttestante;
    protected String localitaAttestante;
    protected String provinciaAttestante;
    protected String nazioneAttestante;

    /**
     * Recupera il valore della proprietà identificativoUnivocoAttestante.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public CtIdentificativoUnivoco getIdentificativoUnivocoAttestante() {
        return identificativoUnivocoAttestante;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public void setIdentificativoUnivocoAttestante(CtIdentificativoUnivoco value) {
        this.identificativoUnivocoAttestante = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneAttestante() {
        return denominazioneAttestante;
    }

    /**
     * Imposta il valore della proprietà denominazioneAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneAttestante(String value) {
        this.denominazioneAttestante = value;
    }

    /**
     * Recupera il valore della proprietà codiceUnitOperAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceUnitOperAttestante() {
        return codiceUnitOperAttestante;
    }

    /**
     * Imposta il valore della proprietà codiceUnitOperAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceUnitOperAttestante(String value) {
        this.codiceUnitOperAttestante = value;
    }

    /**
     * Recupera il valore della proprietà denomUnitOperAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomUnitOperAttestante() {
        return denomUnitOperAttestante;
    }

    /**
     * Imposta il valore della proprietà denomUnitOperAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomUnitOperAttestante(String value) {
        this.denomUnitOperAttestante = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoAttestante() {
        return indirizzoAttestante;
    }

    /**
     * Imposta il valore della proprietà indirizzoAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoAttestante(String value) {
        this.indirizzoAttestante = value;
    }

    /**
     * Recupera il valore della proprietà civicoAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivicoAttestante() {
        return civicoAttestante;
    }

    /**
     * Imposta il valore della proprietà civicoAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivicoAttestante(String value) {
        this.civicoAttestante = value;
    }

    /**
     * Recupera il valore della proprietà capAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapAttestante() {
        return capAttestante;
    }

    /**
     * Imposta il valore della proprietà capAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapAttestante(String value) {
        this.capAttestante = value;
    }

    /**
     * Recupera il valore della proprietà localitaAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalitaAttestante() {
        return localitaAttestante;
    }

    /**
     * Imposta il valore della proprietà localitaAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalitaAttestante(String value) {
        this.localitaAttestante = value;
    }

    /**
     * Recupera il valore della proprietà provinciaAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaAttestante() {
        return provinciaAttestante;
    }

    /**
     * Imposta il valore della proprietà provinciaAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaAttestante(String value) {
        this.provinciaAttestante = value;
    }

    /**
     * Recupera il valore della proprietà nazioneAttestante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazioneAttestante() {
        return nazioneAttestante;
    }

    /**
     * Imposta il valore della proprietà nazioneAttestante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazioneAttestante(String value) {
        this.nazioneAttestante = value;
    }

}
