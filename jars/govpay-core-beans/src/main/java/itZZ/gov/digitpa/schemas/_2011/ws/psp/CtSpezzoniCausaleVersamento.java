
package itZZ.gov.digitpa.schemas._2011.ws.psp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ctSpezzoniCausaleVersamento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ctSpezzoniCausaleVersamento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="6"&gt;
 *         &lt;choice&gt;
 *           &lt;element name="spezzoneCausaleVersamento" type="{http://ws.pagamenti.telematici.gov/}stText35"/&gt;
 *           &lt;element name="spezzoneStrutturatoCausaleVersamento" type="{http://ws.pagamenti.telematici.gov/}ctSpezzoneStrutturatoCausaleVersamento"/&gt;
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
@XmlType(name = "ctSpezzoniCausaleVersamento", propOrder = {
    "spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento"
})
public class CtSpezzoniCausaleVersamento {

    @XmlElements({
        @XmlElement(name = "spezzoneCausaleVersamento", type = String.class),
        @XmlElement(name = "spezzoneStrutturatoCausaleVersamento", type = CtSpezzoneStrutturatoCausaleVersamento.class)
    })
    protected List<Object> spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento;

    /**
     * Gets the value of the spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link CtSpezzoneStrutturatoCausaleVersamento }
     * 
     * 
     */
    public List<Object> getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento() {
        if (this.spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento == null) {
            this.spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento = new ArrayList<>();
        }
        return this.spezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento;
    }

}
