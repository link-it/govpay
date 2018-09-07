
package it.govpay.servizi.v2_3.gpapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Versamento;


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
     * Recupera il valore della proprietà generaIuv.
     * 
     */
    public boolean isGeneraIuv() {
        return this.generaIuv;
    }

    /**
     * Imposta il valore della proprietà generaIuv.
     * 
     */
    public void setGeneraIuv(boolean value) {
        this.generaIuv = value;
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
     * Recupera il valore della proprietà versamento.
     * 
     * @return
     *     possible object is
     *     {@link Versamento }
     *     
     */
    public Versamento getVersamento() {
        return this.versamento;
    }

    /**
     * Imposta il valore della proprietà versamento.
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
