
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctIdentificativoUnivoco complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctIdentificativoUnivoco"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoIdentificativoUnivoco" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stTipoIdentificativoUnivoco"/&gt;
 *         &lt;element name="codiceIdentificativoUnivoco" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctIdentificativoUnivoco", propOrder = {
    "tipoIdentificativoUnivoco",
    "codiceIdentificativoUnivoco"
})
public class CtIdentificativoUnivoco {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StTipoIdentificativoUnivoco tipoIdentificativoUnivoco;
    @XmlElement(required = true)
    protected String codiceIdentificativoUnivoco;

    /**
     * Recupera il valore della proprietà tipoIdentificativoUnivoco.
     * 
     * @return
     *     possible object is
     *     {@link StTipoIdentificativoUnivoco }
     *     
     */
    public StTipoIdentificativoUnivoco getTipoIdentificativoUnivoco() {
        return tipoIdentificativoUnivoco;
    }

    /**
     * Imposta il valore della proprietà tipoIdentificativoUnivoco.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoIdentificativoUnivoco }
     *     
     */
    public void setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco value) {
        this.tipoIdentificativoUnivoco = value;
    }

    /**
     * Recupera il valore della proprietà codiceIdentificativoUnivoco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIdentificativoUnivoco() {
        return codiceIdentificativoUnivoco;
    }

    /**
     * Imposta il valore della proprietà codiceIdentificativoUnivoco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIdentificativoUnivoco(String value) {
        this.codiceIdentificativoUnivoco = value;
    }

}
