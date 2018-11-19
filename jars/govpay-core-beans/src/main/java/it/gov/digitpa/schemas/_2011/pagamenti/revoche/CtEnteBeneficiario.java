
package it.gov.digitpa.schemas._2011.pagamenti.revoche;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctEnteBeneficiario complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctEnteBeneficiario"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}ctIdentificativoUnivocoPersonaG"/&gt;
 *         &lt;element name="denominazioneBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70"/&gt;
 *         &lt;element name="codiceUnitOperBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="denomUnitOperBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="indirizzoBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText70" minOccurs="0"/&gt;
 *         &lt;element name="civicoBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="capBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText16" minOccurs="0"/&gt;
 *         &lt;element name="localitaBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="provinciaBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="nazioneBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stNazione" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctEnteBeneficiario", propOrder = {
    "identificativoUnivocoBeneficiario",
    "denominazioneBeneficiario",
    "codiceUnitOperBeneficiario",
    "denomUnitOperBeneficiario",
    "indirizzoBeneficiario",
    "civicoBeneficiario",
    "capBeneficiario",
    "localitaBeneficiario",
    "provinciaBeneficiario",
    "nazioneBeneficiario"
})
public class CtEnteBeneficiario {

    @XmlElement(required = true)
    protected CtIdentificativoUnivocoPersonaG identificativoUnivocoBeneficiario;
    @XmlElement(required = true)
    protected String denominazioneBeneficiario;
    protected String codiceUnitOperBeneficiario;
    protected String denomUnitOperBeneficiario;
    protected String indirizzoBeneficiario;
    protected String civicoBeneficiario;
    protected String capBeneficiario;
    protected String localitaBeneficiario;
    protected String provinciaBeneficiario;
    protected String nazioneBeneficiario;

    /**
     * Recupera il valore della proprietà identificativoUnivocoBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivocoPersonaG }
     *     
     */
    public CtIdentificativoUnivocoPersonaG getIdentificativoUnivocoBeneficiario() {
        return this.identificativoUnivocoBeneficiario;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivocoPersonaG }
     *     
     */
    public void setIdentificativoUnivocoBeneficiario(CtIdentificativoUnivocoPersonaG value) {
        this.identificativoUnivocoBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneBeneficiario() {
        return this.denominazioneBeneficiario;
    }

    /**
     * Imposta il valore della proprietà denominazioneBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneBeneficiario(String value) {
        this.denominazioneBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà codiceUnitOperBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceUnitOperBeneficiario() {
        return this.codiceUnitOperBeneficiario;
    }

    /**
     * Imposta il valore della proprietà codiceUnitOperBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceUnitOperBeneficiario(String value) {
        this.codiceUnitOperBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà denomUnitOperBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomUnitOperBeneficiario() {
        return this.denomUnitOperBeneficiario;
    }

    /**
     * Imposta il valore della proprietà denomUnitOperBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomUnitOperBeneficiario(String value) {
        this.denomUnitOperBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoBeneficiario() {
        return this.indirizzoBeneficiario;
    }

    /**
     * Imposta il valore della proprietà indirizzoBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoBeneficiario(String value) {
        this.indirizzoBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà civicoBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivicoBeneficiario() {
        return this.civicoBeneficiario;
    }

    /**
     * Imposta il valore della proprietà civicoBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivicoBeneficiario(String value) {
        this.civicoBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà capBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapBeneficiario() {
        return this.capBeneficiario;
    }

    /**
     * Imposta il valore della proprietà capBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapBeneficiario(String value) {
        this.capBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà localitaBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalitaBeneficiario() {
        return this.localitaBeneficiario;
    }

    /**
     * Imposta il valore della proprietà localitaBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalitaBeneficiario(String value) {
        this.localitaBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà provinciaBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaBeneficiario() {
        return this.provinciaBeneficiario;
    }

    /**
     * Imposta il valore della proprietà provinciaBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaBeneficiario(String value) {
        this.provinciaBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà nazioneBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazioneBeneficiario() {
        return this.nazioneBeneficiario;
    }

    /**
     * Imposta il valore della proprietà nazioneBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazioneBeneficiario(String value) {
        this.nazioneBeneficiario = value;
    }

}
