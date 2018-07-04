
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctAllegatoRicevuta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctAllegatoRicevuta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoAllegatoRicevuta" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stTipoAllegatoRicevuta"/&gt;
 *         &lt;element name="testoAllegato" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stBase64"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctAllegatoRicevuta", propOrder = {
    "tipoAllegatoRicevuta",
    "testoAllegato"
})
public class CtAllegatoRicevuta {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StTipoAllegatoRicevuta tipoAllegatoRicevuta;
    @XmlElement(required = true)
    protected byte[] testoAllegato;

    /**
     * Recupera il valore della proprietà tipoAllegatoRicevuta.
     * 
     * @return
     *     possible object is
     *     {@link StTipoAllegatoRicevuta }
     *     
     */
    public StTipoAllegatoRicevuta getTipoAllegatoRicevuta() {
        return tipoAllegatoRicevuta;
    }

    /**
     * Imposta il valore della proprietà tipoAllegatoRicevuta.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoAllegatoRicevuta }
     *     
     */
    public void setTipoAllegatoRicevuta(StTipoAllegatoRicevuta value) {
        this.tipoAllegatoRicevuta = value;
    }

    /**
     * Recupera il valore della proprietà testoAllegato.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getTestoAllegato() {
        return testoAllegato;
    }

    /**
     * Imposta il valore della proprietà testoAllegato.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setTestoAllegato(byte[] value) {
        this.testoAllegato = value;
    }

}
