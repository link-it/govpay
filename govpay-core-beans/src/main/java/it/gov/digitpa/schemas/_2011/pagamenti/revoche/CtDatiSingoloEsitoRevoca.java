
package it.gov.digitpa.schemas._2011.pagamenti.revoche;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Classe Java per ctDatiSingoloEsitoRevoca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctDatiSingoloEsitoRevoca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="singoloImportoRevocato" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stImporto"/&gt;
 *         &lt;element name="identificativoUnivocoRiscossione" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35"/&gt;
 *         &lt;element name="causaleEsito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText140"/&gt;
 *         &lt;element name="datiAggiuntiviEsito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText140"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctDatiSingoloEsitoRevoca", propOrder = {
    "singoloImportoRevocato",
    "identificativoUnivocoRiscossione",
    "causaleEsito",
    "datiAggiuntiviEsito"
})
public class CtDatiSingoloEsitoRevoca {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal singoloImportoRevocato;
    @XmlElement(required = true)
    protected String identificativoUnivocoRiscossione;
    @XmlElement(required = true)
    protected String causaleEsito;
    @XmlElement(required = true)
    protected String datiAggiuntiviEsito;

    /**
     * Recupera il valore della proprietà singoloImportoRevocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getSingoloImportoRevocato() {
        return singoloImportoRevocato;
    }

    /**
     * Imposta il valore della proprietà singoloImportoRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSingoloImportoRevocato(BigDecimal value) {
        this.singoloImportoRevocato = value;
    }

    /**
     * Recupera il valore della proprietà identificativoUnivocoRiscossione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoUnivocoRiscossione() {
        return identificativoUnivocoRiscossione;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoRiscossione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoUnivocoRiscossione(String value) {
        this.identificativoUnivocoRiscossione = value;
    }

    /**
     * Recupera il valore della proprietà causaleEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleEsito() {
        return causaleEsito;
    }

    /**
     * Imposta il valore della proprietà causaleEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleEsito(String value) {
        this.causaleEsito = value;
    }

    /**
     * Recupera il valore della proprietà datiAggiuntiviEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiAggiuntiviEsito() {
        return datiAggiuntiviEsito;
    }

    /**
     * Imposta il valore della proprietà datiAggiuntiviEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiAggiuntiviEsito(String value) {
        this.datiAggiuntiviEsito = value;
    }

}
