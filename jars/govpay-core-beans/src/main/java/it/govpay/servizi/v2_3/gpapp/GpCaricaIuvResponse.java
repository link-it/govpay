
package it.govpay.servizi.v2_3.gpapp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.v2_3.commons.GpResponse;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}gpResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="iuvCaricato" type="{http://www.govpay.it/servizi/commons/}iuvGenerato" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "iuvCaricato"
})
@XmlRootElement(name = "gpCaricaIuvResponse")
public class GpCaricaIuvResponse
    extends GpResponse
{

    protected List<IuvGenerato> iuvCaricato;

    /**
     * Gets the value of the iuvCaricato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iuvCaricato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIuvCaricato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IuvGenerato }
     * 
     * 
     */
    public List<IuvGenerato> getIuvCaricato() {
        if (this.iuvCaricato == null) {
            this.iuvCaricato = new ArrayList<>();
        }
        return this.iuvCaricato;
    }

}
