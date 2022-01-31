
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for iuvGenerato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="iuvGenerato"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="numeroAvviso" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="barCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iuvGenerato", propOrder = {
    "codApplicazione",
    "codVersamentoEnte",
    "codDominio",
    "iuv",
    "numeroAvviso",
    "qrCode",
    "barCode"
})
public class IuvGenerato {

    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true)
    protected String codVersamentoEnte;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected String iuv;
    protected String numeroAvviso;
    @XmlElement(required = true)
    protected byte[] qrCode;
    @XmlElement(required = true)
    protected byte[] barCode;

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
     * Gets the value of the codVersamentoEnte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodVersamentoEnte() {
        return codVersamentoEnte;
    }

    /**
     * Sets the value of the codVersamentoEnte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodVersamentoEnte(String value) {
        this.codVersamentoEnte = value;
    }

    /**
     * Gets the value of the codDominio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDominio() {
        return codDominio;
    }

    /**
     * Sets the value of the codDominio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDominio(String value) {
        this.codDominio = value;
    }

    /**
     * Gets the value of the iuv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIuv() {
        return iuv;
    }

    /**
     * Sets the value of the iuv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIuv(String value) {
        this.iuv = value;
    }

    /**
     * Gets the value of the numeroAvviso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroAvviso() {
        return numeroAvviso;
    }

    /**
     * Sets the value of the numeroAvviso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroAvviso(String value) {
        this.numeroAvviso = value;
    }

    /**
     * Gets the value of the qrCode property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getQrCode() {
        return qrCode;
    }

    /**
     * Sets the value of the qrCode property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setQrCode(byte[] value) {
        this.qrCode = value;
    }

    /**
     * Gets the value of the barCode property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBarCode() {
        return barCode;
    }

    /**
     * Sets the value of the barCode property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBarCode(byte[] value) {
        this.barCode = value;
    }

}
