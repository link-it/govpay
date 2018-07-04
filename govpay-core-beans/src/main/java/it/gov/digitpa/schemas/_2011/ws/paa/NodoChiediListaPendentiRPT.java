
package it.gov.digitpa.schemas._2011.ws.paa;

import java.math.BigInteger;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per nodoChiediListaPendentiRPT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediListaPendentiRPT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="identificativoStazioneIntermediarioPA" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="password" type="{http://ws.pagamenti.telematici.gov/}stPassword"/&gt;
 *         &lt;element name="identificativoDominio" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="rangeDa" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="rangeA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dimensioneLista" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediListaPendentiRPT", propOrder = {
    "identificativoIntermediarioPA",
    "identificativoStazioneIntermediarioPA",
    "password",
    "identificativoDominio",
    "rangeDa",
    "rangeA",
    "dimensioneLista"
})
public class NodoChiediListaPendentiRPT {

    @XmlElement(required = true)
    protected String identificativoIntermediarioPA;
    @XmlElement(required = true)
    protected String identificativoStazioneIntermediarioPA;
    @XmlElement(required = true)
    protected String password;
    protected String identificativoDominio;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date rangeDa;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date rangeA;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger dimensioneLista;

    /**
     * Recupera il valore della proprietà identificativoIntermediarioPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoIntermediarioPA() {
        return identificativoIntermediarioPA;
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
        return identificativoStazioneIntermediarioPA;
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
        return password;
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
        return identificativoDominio;
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
     * Recupera il valore della proprietà rangeDa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getRangeDa() {
        return rangeDa;
    }

    /**
     * Imposta il valore della proprietà rangeDa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeDa(Date value) {
        this.rangeDa = value;
    }

    /**
     * Recupera il valore della proprietà rangeA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getRangeA() {
        return rangeA;
    }

    /**
     * Imposta il valore della proprietà rangeA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeA(Date value) {
        this.rangeA = value;
    }

    /**
     * Recupera il valore della proprietà dimensioneLista.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDimensioneLista() {
        return dimensioneLista;
    }

    /**
     * Imposta il valore della proprietà dimensioneLista.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDimensioneLista(BigInteger value) {
        this.dimensioneLista = value;
    }

}
