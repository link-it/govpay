
package itZZ.gov.digitpa.schemas._2011.ws.nodo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per paaInviaRT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="paaInviaRT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rt" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paaInviaRT", propOrder = {
    "tipoFirma",
    "rt"
})
public class PaaInviaRT {

    @XmlElement(required = true)
    protected String tipoFirma;
    @XmlElement(required = true)
    protected byte[] rt;

    /**
     * Recupera il valore della proprietà tipoFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoFirma() {
        return this.tipoFirma;
    }

    /**
     * Imposta il valore della proprietà tipoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoFirma(String value) {
        this.tipoFirma = value;
    }

    /**
     * Recupera il valore della proprietà rt.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRt() {
        return this.rt;
    }

    /**
     * Imposta il valore della proprietà rt.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRt(byte[] value) {
        this.rt = value;
    }

}
