//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.10.10 alle 02:21:10 PM CEST 
//


package itZZ.gov.agenziaentrate._2014.marcadabollo;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Classe Java per tipoMarcaDaBollo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tipoMarcaDaBollo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PSP" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoPSP"/>
 *         &lt;element name="IUBD" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoTXT"/>
 *         &lt;element name="OraAcquisto" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Importo" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoImporto"/>
 *         &lt;element name="TipoBollo" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoTipoBollo"/>
 *         &lt;element name="ImprontaDocumento" type="{http://www.agenziaentrate.gov.it/2014/MarcaDaBollo}tipoImpronta"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoMarcaDaBollo", propOrder = {
    "psp",
    "iubd",
    "oraAcquisto",
    "importo",
    "tipoBollo",
    "improntaDocumento",
    "signature"
})
public class TipoMarcaDaBollo {

    @XmlElement(name = "PSP", required = true)
    protected TipoPSP psp;
    @XmlElement(name = "IUBD", required = true)
    protected String iubd;
    @XmlElement(name = "OraAcquisto", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar oraAcquisto;
    @XmlElement(name = "Importo", required = true)
    protected BigDecimal importo;
    @XmlElement(name = "TipoBollo", required = true)
    protected String tipoBollo;
    @XmlElement(name = "ImprontaDocumento", required = true)
    protected TipoImpronta improntaDocumento;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;

    /**
     * Recupera il valore della proprietà psp.
     * 
     * @return
     *     possible object is
     *     {@link TipoPSP }
     *     
     */
    public TipoPSP getPSP() {
        return psp;
    }

    /**
     * Imposta il valore della proprietà psp.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPSP }
     *     
     */
    public void setPSP(TipoPSP value) {
        this.psp = value;
    }

    /**
     * Recupera il valore della proprietà iubd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIUBD() {
        return iubd;
    }

    /**
     * Imposta il valore della proprietà iubd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIUBD(String value) {
        this.iubd = value;
    }

    /**
     * Recupera il valore della proprietà oraAcquisto.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOraAcquisto() {
        return oraAcquisto;
    }

    /**
     * Imposta il valore della proprietà oraAcquisto.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOraAcquisto(XMLGregorianCalendar value) {
        this.oraAcquisto = value;
    }

    /**
     * Recupera il valore della proprietà importo.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * Imposta il valore della proprietà importo.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporto(BigDecimal value) {
        this.importo = value;
    }

    /**
     * Recupera il valore della proprietà tipoBollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoBollo() {
        return tipoBollo;
    }

    /**
     * Imposta il valore della proprietà tipoBollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoBollo(String value) {
        this.tipoBollo = value;
    }

    /**
     * Recupera il valore della proprietà improntaDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TipoImpronta }
     *     
     */
    public TipoImpronta getImprontaDocumento() {
        return improntaDocumento;
    }

    /**
     * Imposta il valore della proprietà improntaDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoImpronta }
     *     
     */
    public void setImprontaDocumento(TipoImpronta value) {
        this.improntaDocumento = value;
    }

    /**
     * Recupera il valore della proprietà signature.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Imposta il valore della proprietà signature.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

}
