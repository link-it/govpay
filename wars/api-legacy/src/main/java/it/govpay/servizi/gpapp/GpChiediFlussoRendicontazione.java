
package it.govpay.servizi.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter5;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}gYear"/&gt;
 *         &lt;element name="codFlusso" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codApplicazione",
    "annoRiferimento",
    "codFlusso"
})
@XmlRootElement(name = "gpChiediFlussoRendicontazione")
public class GpChiediFlussoRendicontazione {

    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter5 .class)
    @XmlSchemaType(name = "gYear")
    protected Integer annoRiferimento;
    @XmlElement(required = true)
    protected String codFlusso;

    /**
     * Gets the value of the codApplicazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodApplicazione() {
        return codApplicazione;
    }

    /**
     * Sets the value of the codApplicazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodApplicazione(String value) {
        this.codApplicazione = value;
    }

    /**
     * Gets the value of the annoRiferimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAnnoRiferimento() {
        return annoRiferimento;
    }

    /**
     * Sets the value of the annoRiferimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoRiferimento(Integer value) {
        this.annoRiferimento = value;
    }

    /**
     * Gets the value of the codFlusso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFlusso() {
        return codFlusso;
    }

    /**
     * Sets the value of the codFlusso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFlusso(String value) {
        this.codFlusso = value;
    }

}
