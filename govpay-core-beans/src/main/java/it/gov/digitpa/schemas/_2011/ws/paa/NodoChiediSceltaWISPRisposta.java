
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediSceltaWISPRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediSceltaWISPRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="effettuazioneScelta" type="{http://ws.pagamenti.telematici.gov/}stEffettuazioneScelta" minOccurs="0"/&gt;
 *         &lt;element name="identificativoPSP" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="identificativoIntermediarioPSP" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="identificativoCanale" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="tipoVersamento" type="{http://ws.pagamenti.telematici.gov/}stTipoVersamento" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediSceltaWISPRisposta", propOrder = {
    "effettuazioneScelta",
    "identificativoPSP",
    "identificativoIntermediarioPSP",
    "identificativoCanale",
    "tipoVersamento"
})
public class NodoChiediSceltaWISPRisposta
    extends Risposta
{

    @XmlSchemaType(name = "string")
    protected StEffettuazioneScelta effettuazioneScelta;
    protected String identificativoPSP;
    protected String identificativoIntermediarioPSP;
    protected String identificativoCanale;
    @XmlSchemaType(name = "string")
    protected StTipoVersamento tipoVersamento;

    /**
     * Recupera il valore della proprietà effettuazioneScelta.
     * 
     * @return
     *     possible object is
     *     {@link StEffettuazioneScelta }
     *     
     */
    public StEffettuazioneScelta getEffettuazioneScelta() {
        return this.effettuazioneScelta;
    }

    /**
     * Imposta il valore della proprietà effettuazioneScelta.
     * 
     * @param value
     *     allowed object is
     *     {@link StEffettuazioneScelta }
     *     
     */
    public void setEffettuazioneScelta(StEffettuazioneScelta value) {
        this.effettuazioneScelta = value;
    }

    /**
     * Recupera il valore della proprietà identificativoPSP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoPSP() {
        return this.identificativoPSP;
    }

    /**
     * Imposta il valore della proprietà identificativoPSP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoPSP(String value) {
        this.identificativoPSP = value;
    }

    /**
     * Recupera il valore della proprietà identificativoIntermediarioPSP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoIntermediarioPSP() {
        return this.identificativoIntermediarioPSP;
    }

    /**
     * Imposta il valore della proprietà identificativoIntermediarioPSP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoIntermediarioPSP(String value) {
        this.identificativoIntermediarioPSP = value;
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
     * Recupera il valore della proprietà tipoVersamento.
     * 
     * @return
     *     possible object is
     *     {@link StTipoVersamento }
     *     
     */
    public StTipoVersamento getTipoVersamento() {
        return this.tipoVersamento;
    }

    /**
     * Imposta il valore della proprietà tipoVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoVersamento }
     *     
     */
    public void setTipoVersamento(StTipoVersamento value) {
        this.tipoVersamento = value;
    }

}
