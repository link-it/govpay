
package it.gov.digitpa.schemas._2011.pagamenti.revoche;

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
 *         &lt;element name="identificativoUnivocoMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctIdentificativoUnivoco"/&gt;
 *         &lt;element name="denominazioneMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70"/&gt;
 *         &lt;element name="codiceUnitOperMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="denomUnitOperMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="indirizzoMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="civicoMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="capMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="localitaMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="provinciaMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="nazioneMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stNazione" minOccurs="0"/&gt;
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
    "identificativoUnivocoMittente",
    "denominazioneMittente",
    "codiceUnitOperMittente",
    "denomUnitOperMittente",
    "indirizzoMittente",
    "civicoMittente",
    "capMittente",
    "localitaMittente",
    "provinciaMittente",
    "nazioneMittente"
})
public class CtIstitutoAttestante {

    @XmlElement(required = true)
    protected CtIdentificativoUnivoco identificativoUnivocoMittente;
    @XmlElement(required = true)
    protected String denominazioneMittente;
    protected String codiceUnitOperMittente;
    protected String denomUnitOperMittente;
    protected String indirizzoMittente;
    protected String civicoMittente;
    protected String capMittente;
    protected String localitaMittente;
    protected String provinciaMittente;
    protected String nazioneMittente;

    /**
     * Recupera il valore della proprietà identificativoUnivocoMittente.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public CtIdentificativoUnivoco getIdentificativoUnivocoMittente() {
        return identificativoUnivocoMittente;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public void setIdentificativoUnivocoMittente(CtIdentificativoUnivoco value) {
        this.identificativoUnivocoMittente = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneMittente() {
        return denominazioneMittente;
    }

    /**
     * Imposta il valore della proprietà denominazioneMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneMittente(String value) {
        this.denominazioneMittente = value;
    }

    /**
     * Recupera il valore della proprietà codiceUnitOperMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceUnitOperMittente() {
        return codiceUnitOperMittente;
    }

    /**
     * Imposta il valore della proprietà codiceUnitOperMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceUnitOperMittente(String value) {
        this.codiceUnitOperMittente = value;
    }

    /**
     * Recupera il valore della proprietà denomUnitOperMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomUnitOperMittente() {
        return denomUnitOperMittente;
    }

    /**
     * Imposta il valore della proprietà denomUnitOperMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomUnitOperMittente(String value) {
        this.denomUnitOperMittente = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoMittente() {
        return indirizzoMittente;
    }

    /**
     * Imposta il valore della proprietà indirizzoMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoMittente(String value) {
        this.indirizzoMittente = value;
    }

    /**
     * Recupera il valore della proprietà civicoMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivicoMittente() {
        return civicoMittente;
    }

    /**
     * Imposta il valore della proprietà civicoMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivicoMittente(String value) {
        this.civicoMittente = value;
    }

    /**
     * Recupera il valore della proprietà capMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapMittente() {
        return capMittente;
    }

    /**
     * Imposta il valore della proprietà capMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapMittente(String value) {
        this.capMittente = value;
    }

    /**
     * Recupera il valore della proprietà localitaMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalitaMittente() {
        return localitaMittente;
    }

    /**
     * Imposta il valore della proprietà localitaMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalitaMittente(String value) {
        this.localitaMittente = value;
    }

    /**
     * Recupera il valore della proprietà provinciaMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaMittente() {
        return provinciaMittente;
    }

    /**
     * Imposta il valore della proprietà provinciaMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaMittente(String value) {
        this.provinciaMittente = value;
    }

    /**
     * Recupera il valore della proprietà nazioneMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazioneMittente() {
        return nazioneMittente;
    }

    /**
     * Imposta il valore della proprietà nazioneMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazioneMittente(String value) {
        this.nazioneMittente = value;
    }

}
