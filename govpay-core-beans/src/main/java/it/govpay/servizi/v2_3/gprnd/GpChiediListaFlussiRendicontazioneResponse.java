
package it.govpay.servizi.v2_3.gprnd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.v2_3.commons.EstremiFlussoRendicontazione;
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
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="flussoRendicontazione" type="{http://www.govpay.it/servizi/v2_3/commons/}estremiFlussoRendicontazione"/&gt;
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
    "flussoRendicontazione"
})
@XmlRootElement(name = "gpChiediListaFlussiRendicontazioneResponse")
public class GpChiediListaFlussiRendicontazioneResponse
    extends GpResponse
{

    protected List<EstremiFlussoRendicontazione> flussoRendicontazione;

    /**
     * Gets the value of the flussoRendicontazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flussoRendicontazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlussoRendicontazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EstremiFlussoRendicontazione }
     * 
     * 
     */
    public List<EstremiFlussoRendicontazione> getFlussoRendicontazione() {
        if (flussoRendicontazione == null) {
            flussoRendicontazione = new ArrayList<EstremiFlussoRendicontazione>();
        }
        return this.flussoRendicontazione;
    }

}
