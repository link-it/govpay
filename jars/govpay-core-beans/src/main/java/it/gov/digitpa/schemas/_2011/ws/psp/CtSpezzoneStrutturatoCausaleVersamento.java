
package it.gov.digitpa.schemas._2011.ws.psp;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Classe Java per ctSpezzoneStrutturatoCausaleVersamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctSpezzoneStrutturatoCausaleVersamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="causaleSpezzone" type="{http://ws.pagamenti.telematici.gov/}stText25"/&gt;
 *         &lt;element name="importoSpezzone" type="{http://ws.pagamenti.telematici.gov/}stImporto"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctSpezzoneStrutturatoCausaleVersamento", propOrder = {
    "causaleSpezzone",
    "importoSpezzone"
})
public class CtSpezzoneStrutturatoCausaleVersamento {

    @XmlElement(required = true)
    protected String causaleSpezzone;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoSpezzone;

    /**
     * Recupera il valore della proprietà causaleSpezzone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleSpezzone() {
        return this.causaleSpezzone;
    }

    /**
     * Imposta il valore della proprietà causaleSpezzone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleSpezzone(String value) {
        this.causaleSpezzone = value;
    }

    /**
     * Recupera il valore della proprietà importoSpezzone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoSpezzone() {
        return this.importoSpezzone;
    }

    /**
     * Imposta il valore della proprietà importoSpezzone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoSpezzone(BigDecimal value) {
        this.importoSpezzone = value;
    }

}
