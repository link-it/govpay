//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.07.10 alle 10:18:53 AM CEST 
//


package gov.telematici.pagamenti.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctEsitoPresaInCarico complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctEsitoPresaInCarico">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificativoFlusso" type="{http://ws.pagamenti.telematici.gov/}stIdentificativoFlusso"/>
 *         &lt;element name="codiceEsitoPresaInCarico" type="{http://ws.pagamenti.telematici.gov/}stCodiceEsitoPresaInCarico"/>
 *         &lt;element name="descrizioneEsitoPresaInCarico" type="{http://ws.pagamenti.telematici.gov/}stText140" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctEsitoPresaInCarico", propOrder = {
    "identificativoFlusso",
    "codiceEsitoPresaInCarico",
    "descrizioneEsitoPresaInCarico"
})
public class CtEsitoPresaInCarico {

    @XmlElement(required = true)
    protected String identificativoFlusso;
    @XmlElement(required = true)
    protected BigInteger codiceEsitoPresaInCarico;
    protected String descrizioneEsitoPresaInCarico;

    /**
     * Recupera il valore della proprietà identificativoFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoFlusso() {
        return identificativoFlusso;
    }

    /**
     * Imposta il valore della proprietà identificativoFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoFlusso(String value) {
        this.identificativoFlusso = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsitoPresaInCarico.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCodiceEsitoPresaInCarico() {
        return codiceEsitoPresaInCarico;
    }

    /**
     * Imposta il valore della proprietà codiceEsitoPresaInCarico.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCodiceEsitoPresaInCarico(BigInteger value) {
        this.codiceEsitoPresaInCarico = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneEsitoPresaInCarico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneEsitoPresaInCarico() {
        return descrizioneEsitoPresaInCarico;
    }

    /**
     * Imposta il valore della proprietà descrizioneEsitoPresaInCarico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneEsitoPresaInCarico(String value) {
        this.descrizioneEsitoPresaInCarico = value;
    }

}
