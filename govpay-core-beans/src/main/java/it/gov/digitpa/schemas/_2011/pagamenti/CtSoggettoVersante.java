
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctSoggettoVersante complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctSoggettoVersante"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIdentificativoUnivocoPersonaFG"/&gt;
 *         &lt;element name="anagraficaVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70"/&gt;
 *         &lt;element name="indirizzoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="civicoVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="capVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="localitaVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="provinciaVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="nazioneVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stNazioneProvincia" minOccurs="0"/&gt;
 *         &lt;element name="e-mailVersante" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stEMail" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctSoggettoVersante", propOrder = {
    "identificativoUnivocoVersante",
    "anagraficaVersante",
    "indirizzoVersante",
    "civicoVersante",
    "capVersante",
    "localitaVersante",
    "provinciaVersante",
    "nazioneVersante",
    "eMailVersante"
})
public class CtSoggettoVersante {

    @XmlElement(required = true)
    protected CtIdentificativoUnivocoPersonaFG identificativoUnivocoVersante;
    @XmlElement(required = true)
    protected String anagraficaVersante;
    protected String indirizzoVersante;
    protected String civicoVersante;
    protected String capVersante;
    protected String localitaVersante;
    protected String provinciaVersante;
    protected String nazioneVersante;
    @XmlElement(name = "e-mailVersante")
    protected String eMailVersante;

    /**
     * Recupera il valore della proprietà identificativoUnivocoVersante.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivocoPersonaFG }
     *     
     */
    public CtIdentificativoUnivocoPersonaFG getIdentificativoUnivocoVersante() {
        return identificativoUnivocoVersante;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivocoPersonaFG }
     *     
     */
    public void setIdentificativoUnivocoVersante(CtIdentificativoUnivocoPersonaFG value) {
        this.identificativoUnivocoVersante = value;
    }

    /**
     * Recupera il valore della proprietà anagraficaVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnagraficaVersante() {
        return anagraficaVersante;
    }

    /**
     * Imposta il valore della proprietà anagraficaVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnagraficaVersante(String value) {
        this.anagraficaVersante = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoVersante() {
        return indirizzoVersante;
    }

    /**
     * Imposta il valore della proprietà indirizzoVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoVersante(String value) {
        this.indirizzoVersante = value;
    }

    /**
     * Recupera il valore della proprietà civicoVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivicoVersante() {
        return civicoVersante;
    }

    /**
     * Imposta il valore della proprietà civicoVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivicoVersante(String value) {
        this.civicoVersante = value;
    }

    /**
     * Recupera il valore della proprietà capVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapVersante() {
        return capVersante;
    }

    /**
     * Imposta il valore della proprietà capVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapVersante(String value) {
        this.capVersante = value;
    }

    /**
     * Recupera il valore della proprietà localitaVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalitaVersante() {
        return localitaVersante;
    }

    /**
     * Imposta il valore della proprietà localitaVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalitaVersante(String value) {
        this.localitaVersante = value;
    }

    /**
     * Recupera il valore della proprietà provinciaVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaVersante() {
        return provinciaVersante;
    }

    /**
     * Imposta il valore della proprietà provinciaVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaVersante(String value) {
        this.provinciaVersante = value;
    }

    /**
     * Recupera il valore della proprietà nazioneVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazioneVersante() {
        return nazioneVersante;
    }

    /**
     * Imposta il valore della proprietà nazioneVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazioneVersante(String value) {
        this.nazioneVersante = value;
    }

    /**
     * Recupera il valore della proprietà eMailVersante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMailVersante() {
        return eMailVersante;
    }

    /**
     * Imposta il valore della proprietà eMailVersante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMailVersante(String value) {
        this.eMailVersante = value;
    }

}
