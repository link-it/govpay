
package it.gov.digitpa.schemas._2011.ws.paa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tipoListaRPTPendenti complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tipoListaRPTPendenti"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="totRestituiti" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="rptPendente" type="{http://ws.pagamenti.telematici.gov/}tipoRPTPendente" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoListaRPTPendenti", propOrder = {
    "totRestituiti",
    "rptPendente"
})
public class TipoListaRPTPendenti {

    protected int totRestituiti;
    @XmlElement(nillable = true)
    protected List<TipoRPTPendente> rptPendente;

    /**
     * Recupera il valore della proprietà totRestituiti.
     * 
     */
    public int getTotRestituiti() {
        return totRestituiti;
    }

    /**
     * Imposta il valore della proprietà totRestituiti.
     * 
     */
    public void setTotRestituiti(int value) {
        this.totRestituiti = value;
    }

    /**
     * Gets the value of the rptPendente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rptPendente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRptPendente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoRPTPendente }
     * 
     * 
     */
    public List<TipoRPTPendente> getRptPendente() {
        if (rptPendente == null) {
            rptPendente = new ArrayList<TipoRPTPendente>();
        }
        return this.rptPendente;
    }

}
