
package it.govpay.servizi.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.TipoAutenticazione;
import it.govpay.servizi.commons.Versamento;
import it.govpay.servizi.commons.VersamentoKey;


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
 *         &lt;element name="codConvenzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codSessionePortale" type="{http://www.govpay.it/servizi/commons/}uuid" minOccurs="0"/&gt;
 *         &lt;element name="codPortale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="versante" type="{http://www.govpay.it/servizi/commons/}anagrafica" minOccurs="0"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="canale" type="{http://www.govpay.it/servizi/commons/}canale"/&gt;
 *           &lt;element name="sceltaWisp"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                     &lt;element name="codKeyPA" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
 *                     &lt;element name="codKeyWISP" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="ibanAddebito" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;element name="autenticazione" type="{http://www.govpay.it/servizi/commons/}tipoAutenticazione"/&gt;
 *         &lt;element name="urlRitorno" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="aggiornaSeEsiste" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;element name="versamento" type="{http://www.govpay.it/servizi/commons/}versamento"/&gt;
 *           &lt;element name="versamentoRef" type="{http://www.govpay.it/servizi/commons/}versamentoKey"/&gt;
 *         &lt;/choice&gt;
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
    "codConvenzione",
    "codSessionePortale",
    "codPortale",
    "versante",
    "canale",
    "sceltaWisp",
    "ibanAddebito",
    "autenticazione",
    "urlRitorno",
    "aggiornaSeEsiste",
    "versamentoOrVersamentoRef"
})
@XmlRootElement(name = "gpAvviaTransazionePagamento")
public class GpAvviaTransazionePagamento {

    protected String codConvenzione;
    protected String codSessionePortale;
    @XmlElement(required = true)
    protected String codPortale;
    protected Anagrafica versante;
    protected Canale canale;
    protected GpAvviaTransazionePagamento.SceltaWisp sceltaWisp;
    protected String ibanAddebito;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TipoAutenticazione autenticazione;
    @XmlSchemaType(name = "anyURI")
    protected String urlRitorno;
    @XmlElement(defaultValue = "true")
    protected Boolean aggiornaSeEsiste;
    @XmlElements({
        @XmlElement(name = "versamento", type = Versamento.class),
        @XmlElement(name = "versamentoRef", type = VersamentoKey.class)
    })
    protected List<Object> versamentoOrVersamentoRef;

    /**
     * Gets the value of the codConvenzione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodConvenzione() {
        return codConvenzione;
    }

    /**
     * Sets the value of the codConvenzione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodConvenzione(String value) {
        this.codConvenzione = value;
    }

    /**
     * Gets the value of the codSessionePortale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSessionePortale() {
        return codSessionePortale;
    }

    /**
     * Sets the value of the codSessionePortale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSessionePortale(String value) {
        this.codSessionePortale = value;
    }

    /**
     * Gets the value of the codPortale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPortale() {
        return codPortale;
    }

    /**
     * Sets the value of the codPortale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPortale(String value) {
        this.codPortale = value;
    }

    /**
     * Gets the value of the versante property.
     * 
     * @return
     *     possible object is
     *     {@link Anagrafica }
     *     
     */
    public Anagrafica getVersante() {
        return versante;
    }

    /**
     * Sets the value of the versante property.
     * 
     * @param value
     *     allowed object is
     *     {@link Anagrafica }
     *     
     */
    public void setVersante(Anagrafica value) {
        this.versante = value;
    }

    /**
     * Gets the value of the canale property.
     * 
     * @return
     *     possible object is
     *     {@link Canale }
     *     
     */
    public Canale getCanale() {
        return canale;
    }

    /**
     * Sets the value of the canale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Canale }
     *     
     */
    public void setCanale(Canale value) {
        this.canale = value;
    }

    /**
     * Gets the value of the sceltaWisp property.
     * 
     * @return
     *     possible object is
     *     {@link GpAvviaTransazionePagamento.SceltaWisp }
     *     
     */
    public GpAvviaTransazionePagamento.SceltaWisp getSceltaWisp() {
        return sceltaWisp;
    }

    /**
     * Sets the value of the sceltaWisp property.
     * 
     * @param value
     *     allowed object is
     *     {@link GpAvviaTransazionePagamento.SceltaWisp }
     *     
     */
    public void setSceltaWisp(GpAvviaTransazionePagamento.SceltaWisp value) {
        this.sceltaWisp = value;
    }

    /**
     * Gets the value of the ibanAddebito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanAddebito() {
        return ibanAddebito;
    }

    /**
     * Sets the value of the ibanAddebito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanAddebito(String value) {
        this.ibanAddebito = value;
    }

    /**
     * Gets the value of the autenticazione property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAutenticazione }
     *     
     */
    public TipoAutenticazione getAutenticazione() {
        return autenticazione;
    }

    /**
     * Sets the value of the autenticazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAutenticazione }
     *     
     */
    public void setAutenticazione(TipoAutenticazione value) {
        this.autenticazione = value;
    }

    /**
     * Gets the value of the urlRitorno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRitorno() {
        return urlRitorno;
    }

    /**
     * Sets the value of the urlRitorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRitorno(String value) {
        this.urlRitorno = value;
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
     * Gets the value of the versamentoOrVersamentoRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the versamentoOrVersamentoRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVersamentoOrVersamentoRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Versamento }
     * {@link VersamentoKey }
     * 
     * 
     */
    public List<Object> getVersamentoOrVersamentoRef() {
        if (versamentoOrVersamentoRef == null) {
            versamentoOrVersamentoRef = new ArrayList<Object>();
        }
        return this.versamentoOrVersamentoRef;
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
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codKeyPA" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
     *         &lt;element name="codKeyWISP" type="{http://www.govpay.it/servizi/commons/}string40"/&gt;
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
        "codDominio",
        "codKeyPA",
        "codKeyWISP"
    })
    public static class SceltaWisp {

        @XmlElement(required = true)
        protected String codDominio;
        @XmlElement(required = true)
        protected String codKeyPA;
        @XmlElement(required = true)
        protected String codKeyWISP;

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
         * Gets the value of the codKeyPA property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodKeyPA() {
            return codKeyPA;
        }

        /**
         * Sets the value of the codKeyPA property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodKeyPA(String value) {
            this.codKeyPA = value;
        }

        /**
         * Gets the value of the codKeyWISP property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodKeyWISP() {
            return codKeyWISP;
        }

        /**
         * Sets the value of the codKeyWISP property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodKeyWISP(String value) {
            this.codKeyWISP = value;
        }

    }

}
