
package itZZ.gov.digitpa.schemas._2011.ws.paa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tipoListaRPT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tipoListaRPT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elementoListaRPT" type="{http://ws.pagamenti.telematici.gov/}tipoElementoListaRPT" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoListaRPT", propOrder = {
    "elementoListaRPT"
})
public class TipoListaRPT {

    @XmlElement(required = true)
    protected List<TipoElementoListaRPT> elementoListaRPT;

    /**
     * Gets the value of the elementoListaRPT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elementoListaRPT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElementoListaRPT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoElementoListaRPT }
     * 
     * 
     */
    public List<TipoElementoListaRPT> getElementoListaRPT() {
        if (this.elementoListaRPT == null) {
            this.elementoListaRPT = new ArrayList<>();
        }
        return this.elementoListaRPT;
    }

}
