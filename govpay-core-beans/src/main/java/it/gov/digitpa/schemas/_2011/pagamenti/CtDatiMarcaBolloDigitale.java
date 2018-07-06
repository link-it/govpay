
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctDatiMarcaBolloDigitale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctDatiMarcaBolloDigitale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoBollo" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stTipoBollo"/&gt;
 *         &lt;element name="hashDocumento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70"/&gt;
 *         &lt;element name="provinciaResidenza" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stNazioneProvincia"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctDatiMarcaBolloDigitale", propOrder = {
    "tipoBollo",
    "hashDocumento",
    "provinciaResidenza"
})
public class CtDatiMarcaBolloDigitale {

    @XmlElement(required = true)
    protected String tipoBollo;
    @XmlElement(required = true)
    protected String hashDocumento;
    @XmlElement(required = true)
    protected String provinciaResidenza;

    /**
     * Recupera il valore della proprietà tipoBollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoBollo() {
        return tipoBollo;
    }

    /**
     * Imposta il valore della proprietà tipoBollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoBollo(String value) {
        this.tipoBollo = value;
    }

    /**
     * Recupera il valore della proprietà hashDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashDocumento() {
        return hashDocumento;
    }

    /**
     * Imposta il valore della proprietà hashDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashDocumento(String value) {
        this.hashDocumento = value;
    }

    /**
     * Recupera il valore della proprietà provinciaResidenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    /**
     * Imposta il valore della proprietà provinciaResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaResidenza(String value) {
        this.provinciaResidenza = value;
    }

}
