
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctIdentificativoUnivocoPersonaG complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctIdentificativoUnivocoPersonaG"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoIdentificativoUnivoco" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stTipoIdentificativoUnivocoPersG"/&gt;
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
@XmlType(name = "ctIdentificativoUnivocoPersonaG", propOrder = {
    "tipoIdentificativoUnivoco",
    "codiceIdentificativoUnivoco"
})
public class CtIdentificativoUnivocoPersonaG {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StTipoIdentificativoUnivocoPersG tipoIdentificativoUnivoco;
    @XmlElement(required = true)
    protected String codiceIdentificativoUnivoco;

    /**
     * Recupera il valore della proprietà tipoIdentificativoUnivoco.
     * 
     * @return
     *     possible object is
     *     {@link StTipoIdentificativoUnivocoPersG }
     *     
     */
    public StTipoIdentificativoUnivocoPersG getTipoIdentificativoUnivoco() {
        return tipoIdentificativoUnivoco;
    }

    /**
     * Imposta il valore della proprietà tipoIdentificativoUnivoco.
     * 
     * @param value
     *     allowed object is
     *     {@link StTipoIdentificativoUnivocoPersG }
     *     
     */
    public void setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG value) {
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
