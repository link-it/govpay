
package it.govpay.servizi.gpapp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.IuvGenerato;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}gpResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="iuvGenerato" type="{http://www.govpay.it/servizi/commons/}iuvGenerato" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "iuvGenerato"
})
@XmlRootElement(name = "gpGeneraIuvResponse")
public class GpGeneraIuvResponse
    extends GpResponse
{

    protected List<IuvGenerato> iuvGenerato;

    /**
     * Gets the value of the iuvGenerato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iuvGenerato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIuvGenerato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IuvGenerato }
     * 
     * 
     */
    public List<IuvGenerato> getIuvGenerato() {
        if (iuvGenerato == null) {
            iuvGenerato = new ArrayList<IuvGenerato>();
        }
        return this.iuvGenerato;
    }

}
