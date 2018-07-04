
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctIstitutoRicevente complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctIstitutoRicevente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoRicevente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIdentificativoUnivocoPersonaG"/&gt;
 *         &lt;element name="denominazioneRicevente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctIstitutoRicevente", propOrder = {
    "identificativoUnivocoRicevente",
    "denominazioneRicevente"
})
public class CtIstitutoRicevente {

    @XmlElement(required = true)
    protected CtIdentificativoUnivocoPersonaG identificativoUnivocoRicevente;
    protected String denominazioneRicevente;

    /**
     * Recupera il valore della proprietà identificativoUnivocoRicevente.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivocoPersonaG }
     *     
     */
    public CtIdentificativoUnivocoPersonaG getIdentificativoUnivocoRicevente() {
        return identificativoUnivocoRicevente;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivocoPersonaG }
     *     
     */
    public void setIdentificativoUnivocoRicevente(CtIdentificativoUnivocoPersonaG value) {
        this.identificativoUnivocoRicevente = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneRicevente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneRicevente() {
        return denominazioneRicevente;
    }

    /**
     * Imposta il valore della proprietà denominazioneRicevente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneRicevente(String value) {
        this.denominazioneRicevente = value;
    }

}
