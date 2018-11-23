
package itZZ.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoInviaRPT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoInviaRPT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="password" type="{http://ws.pagamenti.telematici.gov/}stPassword"/&gt;
 *         &lt;element name="identificativoPSP" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *         &lt;element name="identificativoIntermediarioPSP" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="identificativoCanale" type="{http://ws.pagamenti.telematici.gov/}stText35" minOccurs="0"/&gt;
 *         &lt;element name="tipoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rpt" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoInviaRPT", propOrder = {
    "password",
    "identificativoPSP",
    "identificativoIntermediarioPSP",
    "identificativoCanale",
    "tipoFirma",
    "rpt"
})
public class NodoInviaRPT {

    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected String identificativoPSP;
    protected String identificativoIntermediarioPSP;
    protected String identificativoCanale;
    @XmlElement(required = true)
    protected String tipoFirma;
    @XmlElement(required = true)
    protected byte[] rpt;

    /**
     * Recupera il valore della proprietà password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Imposta il valore della proprietà password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
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
     * Recupera il valore della proprietà rpt.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRpt() {
        return this.rpt;
    }

    /**
     * Imposta il valore della proprietà rpt.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRpt(byte[] value) {
        this.rpt = value;
    }

}
