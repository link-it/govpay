
package itZZ.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediSceltaWISP complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediSceltaWISP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="identificativoStazioneIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="password" type="{http://ws.pagamenti.telematici.gov/}stPassword"/&gt;
 *         &lt;element name="identificativoDominio" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="keyPA" type="{http://ws.pagamenti.telematici.gov/}stText40"/&gt;
 *         &lt;element name="keyWISP" type="{http://ws.pagamenti.telematici.gov/}stText40"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediSceltaWISP", propOrder = {
    "identificativoIntermediarioPA",
    "identificativoStazioneIntermediarioPA",
    "password",
    "identificativoDominio",
    "keyPA",
    "keyWISP"
})
public class NodoChiediSceltaWISP {

    @XmlElement(required = true)
    protected String identificativoIntermediarioPA;
    @XmlElement(required = true)
    protected String identificativoStazioneIntermediarioPA;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected String identificativoDominio;
    @XmlElement(required = true)
    protected String keyPA;
    @XmlElement(required = true)
    protected String keyWISP;

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
     * Recupera il valore della proprietà password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Imposta il valore della proprietà password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Recupera il valore della proprietà identificativoDominio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoDominio() {
        return this.identificativoDominio;
    }

    /**
     * Imposta il valore della proprietà identificativoDominio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoDominio(String value) {
        this.identificativoDominio = value;
    }

    /**
     * Recupera il valore della proprietà keyPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyPA() {
        return this.keyPA;
    }

    /**
     * Imposta il valore della proprietà keyPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyPA(String value) {
        this.keyPA = value;
    }

    /**
     * Recupera il valore della proprietà keyWISP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyWISP() {
        return this.keyWISP;
    }

    /**
     * Imposta il valore della proprietà keyWISP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyWISP(String value) {
        this.keyWISP = value;
    }

}
