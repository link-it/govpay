//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.07.10 alle 10:18:53 AM CEST 
//


package gov.telematici.pagamenti.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ctEsitoAvvisatura complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctEsitoAvvisatura">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoCanaleEsito" type="{http://ws.pagamenti.telematici.gov/}stTipoCanaleEsito"/>
 *         &lt;element name="identificativoCanale" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/>
 *         &lt;element name="dataEsito" type="{http://ws.pagamenti.telematici.gov/}stISODate"/>
 *         &lt;element name="codiceEsito" type="{http://ws.pagamenti.telematici.gov/}stCodiceEsito"/>
 *         &lt;element name="descrizioneEsito" type="{http://ws.pagamenti.telematici.gov/}stText140" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctEsitoAvvisatura", propOrder = {
    "tipoCanaleEsito",
    "identificativoCanale",
    "dataEsito",
    "codiceEsito",
    "descrizioneEsito"
})
public class CtEsitoAvvisatura {

    @XmlElement(required = true)
    protected String tipoCanaleEsito;
    protected String identificativoCanale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataEsito;
    @XmlSchemaType(name = "integer")
    protected int codiceEsito;
    protected String descrizioneEsito;

    /**
     * Recupera il valore della proprietà tipoCanaleEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCanaleEsito() {
        return this.tipoCanaleEsito;
    }

    /**
     * Imposta il valore della proprietà tipoCanaleEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCanaleEsito(String value) {
        this.tipoCanaleEsito = value;
    }

    /**
     * Recupera il valore della proprietà identificativoCanale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoCanale() {
        return this.identificativoCanale;
    }

    /**
     * Imposta il valore della proprietà identificativoCanale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoCanale(String value) {
        this.identificativoCanale = value;
    }

    /**
     * Recupera il valore della proprietà dataEsito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEsito() {
        return this.dataEsito;
    }

    /**
     * Imposta il valore della proprietà dataEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEsito(XMLGregorianCalendar value) {
        this.dataEsito = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsito.
     * 
     */
    public int getCodiceEsito() {
        return this.codiceEsito;
    }

    /**
     * Imposta il valore della proprietà codiceEsito.
     * 
     */
    public void setCodiceEsito(int value) {
        this.codiceEsito = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneEsito() {
        return this.descrizioneEsito;
    }

    /**
     * Imposta il valore della proprietà descrizioneEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneEsito(String value) {
        this.descrizioneEsito = value;
    }

}
