
package it.gov.digitpa.schemas._2011.ws.psp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per faultBean complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="faultBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="faultCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="faultString" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serial" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "faultBean", propOrder = {
    "faultCode",
    "faultString",
    "id",
    "description",
    "serial"
})
public class FaultBean {

    @XmlElement(required = true)
    protected String faultCode;
    @XmlElement(required = true)
    protected String faultString;
    @XmlElement(required = true)
    protected String id;
    protected String description;
    protected Integer serial;

    /**
     * Recupera il valore della proprietà faultCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultCode() {
        return this.faultCode;
    }

    /**
     * Imposta il valore della proprietà faultCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultCode(String value) {
        this.faultCode = value;
    }

    /**
     * Recupera il valore della proprietà faultString.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultString() {
        return this.faultString;
    }

    /**
     * Imposta il valore della proprietà faultString.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultString(String value) {
        this.faultString = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return this.id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprietà description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Imposta il valore della proprietà description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Recupera il valore della proprietà serial.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSerial() {
        return this.serial;
    }

    /**
     * Imposta il valore della proprietà serial.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSerial(Integer value) {
        this.serial = value;
    }

}
