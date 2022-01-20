
package it.govpay.servizi.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Versamento;


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
 *         &lt;element name="generaIuv" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="aggiornaSeEsiste" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="versamento" type="{http://www.govpay.it/servizi/commons/}versamento"/&gt;
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
    "generaIuv",
    "aggiornaSeEsiste",
    "versamento"
})
@XmlRootElement(name = "gpCaricaVersamento")
public class GpCaricaVersamento {

    protected boolean generaIuv;
    @XmlElement(defaultValue = "true")
    protected Boolean aggiornaSeEsiste;
    @XmlElement(required = true)
    protected Versamento versamento;

    /**
     * Gets the value of the generaIuv property.
     * 
     */
    public boolean isGeneraIuv() {
        return generaIuv;
    }

    /**
     * Sets the value of the generaIuv property.
     * 
     */
    public void setGeneraIuv(boolean value) {
        this.generaIuv = value;
    }

    /**
     * Gets the value of the aggiornaSeEsiste property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAggiornaSeEsiste() {
        return aggiornaSeEsiste;
    }

    /**
     * Sets the value of the aggiornaSeEsiste property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAggiornaSeEsiste(Boolean value) {
        this.aggiornaSeEsiste = value;
    }

    /**
     * Gets the value of the versamento property.
     * 
     * @return
     *     possible object is
     *     {@link Versamento }
     *     
     */
    public Versamento getVersamento() {
        return versamento;
    }

    /**
     * Sets the value of the versamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Versamento }
     *     
     */
    public void setVersamento(Versamento value) {
        this.versamento = value;
    }

}
