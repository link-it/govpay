
package it.govpay.servizi.gpapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


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
 *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="iuvRichiesto" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "codDominio",
    "iuvRichiesto"
})
@XmlRootElement(name = "gpGeneraIuv")
public class GpGeneraIuv {

    @XmlElement(required = true)
    protected String codApplicazione;
    @XmlElement(required = true)
    protected String codDominio;
    @XmlElement(required = true)
    protected List<GpGeneraIuv.IuvRichiesto> iuvRichiesto;

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
     * Gets the value of the iuvRichiesto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iuvRichiesto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIuvRichiesto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpGeneraIuv.IuvRichiesto }
     * 
     * 
     */
    public List<GpGeneraIuv.IuvRichiesto> getIuvRichiesto() {
        if (iuvRichiesto == null) {
            iuvRichiesto = new ArrayList<GpGeneraIuv.IuvRichiesto>();
        }
        return this.iuvRichiesto;
    }


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
     *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
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
        "codVersamentoEnte",
        "importoTotale"
    })
    public static class IuvRichiesto {

        @XmlElement(required = true)
        protected String codVersamentoEnte;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importoTotale;

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
         * Gets the value of the importoTotale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImportoTotale() {
            return importoTotale;
        }

        /**
         * Sets the value of the importoTotale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImportoTotale(BigDecimal value) {
            this.importoTotale = value;
        }

    }

}
