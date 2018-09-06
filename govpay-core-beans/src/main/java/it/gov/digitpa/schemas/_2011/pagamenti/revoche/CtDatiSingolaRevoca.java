
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
 * <p>Classe Java per ctDatiSingolaRevoca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctDatiSingolaRevoca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="singoloImportoRevocato" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stImporto"/&gt;
 *         &lt;element name="identificativoUnivocoRiscossione" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText35"/&gt;
 *         &lt;element name="causaleRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText140"/&gt;
 *         &lt;element name="datiAggiuntiviRevoca" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/}stText140"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctDatiSingolaRevoca", propOrder = {
    "singoloImportoRevocato",
    "identificativoUnivocoRiscossione",
    "causaleRevoca",
    "datiAggiuntiviRevoca"
})
public class CtDatiSingolaRevoca {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal singoloImportoRevocato;
    @XmlElement(required = true)
    protected String identificativoUnivocoRiscossione;
    @XmlElement(required = true)
    protected String causaleRevoca;
    @XmlElement(required = true)
    protected String datiAggiuntiviRevoca;

    /**
     * Recupera il valore della proprietà singoloImportoRevocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getSingoloImportoRevocato() {
        return this.singoloImportoRevocato;
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
        return this.identificativoUnivocoRiscossione;
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
     * Recupera il valore della proprietà causaleRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return this.causaleRevoca;
    }

    /**
     * Imposta il valore della proprietà causaleRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleRevoca(String value) {
        this.causaleRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiAggiuntiviRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiAggiuntiviRevoca() {
        return this.datiAggiuntiviRevoca;
    }

    /**
     * Imposta il valore della proprietà datiAggiuntiviRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiAggiuntiviRevoca(String value) {
        this.datiAggiuntiviRevoca = value;
    }

}
