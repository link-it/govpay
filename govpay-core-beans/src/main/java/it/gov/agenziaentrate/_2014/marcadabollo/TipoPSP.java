//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.10.10 alle 02:21:10 PM CEST 
//


package it.gov.agenziaentrate._2014.marcadabollo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tipoPSP complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tipoPSP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodiceFiscale" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoTXT"/>
 *         &lt;element name="Denominazione" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoTXT"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoPSP", propOrder = {
    "codiceFiscale",
    "denominazione"
})
public class TipoPSP {

    @XmlElement(name = "CodiceFiscale", required = true)
    protected String codiceFiscale;
    @XmlElement(name = "Denominazione", required = true)
    protected String denominazione;

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà denominazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazione(String value) {
        this.denominazione = value;
    }

}
