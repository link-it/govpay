//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.07.10 alle 10:18:53 AM CEST 
//


package gov.telematici.pagamenti.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctEsitoAvvisoDigitale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctEsitoAvvisoDigitale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificativoDominio" type="{http://ws.pagamenti.telematici.gov/}stText35"/>
 *         &lt;element name="identificativoMessaggioRichiesta" type="{http://ws.pagamenti.telematici.gov/}stIdentificativoMessaggioRichiesta"/>
 *         &lt;element name="esitoAvvisatura" type="{http://ws.pagamenti.telematici.gov/}ctEsitoAvvisatura" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "esitoAvvisoDigitale", propOrder = {
    "identificativoDominio",
    "identificativoMessaggioRichiesta",
    "esitoAvvisatura"
})
public class CtEsitoAvvisoDigitale {

    @XmlElement(required = true)
    protected String identificativoDominio;
    @XmlElement(required = true)
    protected String identificativoMessaggioRichiesta;
    protected List<CtEsitoAvvisatura> esitoAvvisatura;

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
     * Recupera il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoMessaggioRichiesta() {
        return identificativoMessaggioRichiesta;
    }

    /**
     * Imposta il valore della proprietà identificativoMessaggioRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoMessaggioRichiesta(String value) {
        this.identificativoMessaggioRichiesta = value;
    }

    /**
     * Gets the value of the esitoAvvisatura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the esitoAvvisatura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEsitoAvvisatura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CtEsitoAvvisatura }
     * 
     * 
     */
    public List<CtEsitoAvvisatura> getEsitoAvvisatura() {
        if (esitoAvvisatura == null) {
            esitoAvvisatura = new ArrayList<CtEsitoAvvisatura>();
        }
        return this.esitoAvvisatura;
    }

}
