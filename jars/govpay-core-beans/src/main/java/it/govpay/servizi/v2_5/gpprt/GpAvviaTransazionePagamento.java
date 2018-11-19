
package it.govpay.servizi.v2_5.gpprt;

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
import it.govpay.servizi.commons.TipoAutenticazione;
import it.govpay.servizi.commons.Versamento;
import it.govpay.servizi.commons.VersamentoKey;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codSessionePortale" type="{http://www.govpay.it/servizi/commons/}uuid" minOccurs="0"/&gt;
 *         &lt;element name="codPortale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;element name="versante" type="{http://www.govpay.it/servizi/commons/}anagrafica" minOccurs="0"/&gt;
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
    "codSessionePortale",
    "codPortale",
    "versante",
    "ibanAddebito",
    "autenticazione",
    "urlRitorno",
    "aggiornaSeEsiste",
    "versamentoOrVersamentoRef"
})
@XmlRootElement(name = "gpAvviaTransazionePagamento")
public class GpAvviaTransazionePagamento {

    protected String codSessionePortale;
    @XmlElement(required = true)
    protected String codPortale;
    protected Anagrafica versante;
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
     * Recupera il valore della proprietà codSessionePortale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSessionePortale() {
        return this.codSessionePortale;
    }

    /**
     * Imposta il valore della proprietà codSessionePortale.
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
     * Recupera il valore della proprietà codPortale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPortale() {
        return this.codPortale;
    }

    /**
     * Imposta il valore della proprietà codPortale.
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
     * Recupera il valore della proprietà versante.
     * 
     * @return
     *     possible object is
     *     {@link Anagrafica }
     *     
     */
    public Anagrafica getVersante() {
        return this.versante;
    }

    /**
     * Imposta il valore della proprietà versante.
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
     * Recupera il valore della proprietà ibanAddebito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanAddebito() {
        return this.ibanAddebito;
    }

    /**
     * Imposta il valore della proprietà ibanAddebito.
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
     * Recupera il valore della proprietà autenticazione.
     * 
     * @return
     *     possible object is
     *     {@link TipoAutenticazione }
     *     
     */
    public TipoAutenticazione getAutenticazione() {
        return this.autenticazione;
    }

    /**
     * Imposta il valore della proprietà autenticazione.
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
     * Recupera il valore della proprietà urlRitorno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRitorno() {
        return this.urlRitorno;
    }

    /**
     * Imposta il valore della proprietà urlRitorno.
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
     * Recupera il valore della proprietà aggiornaSeEsiste.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAggiornaSeEsiste() {
        return this.aggiornaSeEsiste;
    }

    /**
     * Imposta il valore della proprietà aggiornaSeEsiste.
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
        if (this.versamentoOrVersamentoRef == null) {
            this.versamentoOrVersamentoRef = new ArrayList<>();
        }
        return this.versamentoOrVersamentoRef;
    }

}
