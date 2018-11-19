
package gov.telematici.pagamenti.ws.ppthead;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import gov.telematici.pagamenti.ws.ppthead.runtime.ZeroOneBooleanAdapter;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/ppthead}stText35"/&gt;
 *         &lt;element name="identificativoStazioneIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/ppthead}stText35"/&gt;
 *         &lt;element name="identificativoCarrello" type="{http://ws.pagamenti.telematici.gov/ppthead}stText35"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://schemas.xmlsoap.org/soap/envelope/}actor"/&gt;
 *       &lt;attribute ref="{http://schemas.xmlsoap.org/soap/envelope/}mustUnderstand"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identificativoIntermediarioPA",
    "identificativoStazioneIntermediarioPA",
    "identificativoCarrello"
})
@XmlRootElement(name = "intestazioneCarrelloPPT")
public class IntestazioneCarrelloPPT {

    @XmlElement(required = true)
    protected String identificativoIntermediarioPA;
    @XmlElement(required = true)
    protected String identificativoStazioneIntermediarioPA;
    @XmlElement(required = true)
    protected String identificativoCarrello;
    @XmlAttribute(name = "actor", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    @XmlSchemaType(name = "anyURI")
    protected String actor;
    @XmlAttribute(name = "mustUnderstand", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    @XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
    protected Boolean mustUnderstand;

    /**
     * Recupera il valore della proprietà identificativoIntermediarioPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoIntermediarioPA() {
        return this.identificativoIntermediarioPA;
    }

    /**
     * Imposta il valore della proprietà identificativoIntermediarioPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoIntermediarioPA(String value) {
        this.identificativoIntermediarioPA = value;
    }

    /**
     * Recupera il valore della proprietà identificativoStazioneIntermediarioPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoStazioneIntermediarioPA() {
        return this.identificativoStazioneIntermediarioPA;
    }

    /**
     * Imposta il valore della proprietà identificativoStazioneIntermediarioPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoStazioneIntermediarioPA(String value) {
        this.identificativoStazioneIntermediarioPA = value;
    }

    /**
     * Recupera il valore della proprietà identificativoCarrello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoCarrello() {
        return this.identificativoCarrello;
    }

    /**
     * Imposta il valore della proprietà identificativoCarrello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoCarrello(String value) {
        this.identificativoCarrello = value;
    }

    /**
     * Recupera il valore della proprietà actor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActor() {
        return this.actor;
    }

    /**
     * Imposta il valore della proprietà actor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActor(String value) {
        this.actor = value;
    }

    /**
     * Recupera il valore della proprietà mustUnderstand.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Boolean isMustUnderstand() {
        return this.mustUnderstand;
    }

    /**
     * Imposta il valore della proprietà mustUnderstand.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMustUnderstand(Boolean value) {
        this.mustUnderstand = value;
    }

}
