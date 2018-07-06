
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctIstitutoMittente complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctIstitutoMittente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoUnivocoMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctIdentificativoUnivoco"/&gt;
 *         &lt;element name="denominazioneMittente" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText70" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctIstitutoMittente", propOrder = {
    "identificativoUnivocoMittente",
    "denominazioneMittente"
})
public class CtIstitutoMittente {

    @XmlElement(required = true)
    protected CtIdentificativoUnivoco identificativoUnivocoMittente;
    protected String denominazioneMittente;

    /**
     * Recupera il valore della proprietà identificativoUnivocoMittente.
     * 
     * @return
     *     possible object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public CtIdentificativoUnivoco getIdentificativoUnivocoMittente() {
        return identificativoUnivocoMittente;
    }

    /**
     * Imposta il valore della proprietà identificativoUnivocoMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link CtIdentificativoUnivoco }
     *     
     */
    public void setIdentificativoUnivocoMittente(CtIdentificativoUnivoco value) {
        this.identificativoUnivocoMittente = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneMittente() {
        return denominazioneMittente;
    }

    /**
     * Imposta il valore della proprietà denominazioneMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneMittente(String value) {
        this.denominazioneMittente = value;
    }

}
