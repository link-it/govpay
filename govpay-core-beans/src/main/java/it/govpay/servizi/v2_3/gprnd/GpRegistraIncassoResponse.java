
package it.govpay.servizi.v2_3.gprnd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.v2_3.commons.GpResponse;
import it.govpay.servizi.v2_3.commons.Incasso;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="incasso" type="{http://www.govpay.it/servizi/v2_3/commons/}incasso"/&gt;
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
    "incasso"
})
@XmlRootElement(name = "gpRegistraIncassoResponse")
public class GpRegistraIncassoResponse
    extends GpResponse
{

    protected Incasso incasso;

    /**
     * Recupera il valore della proprietà incasso.
     * 
     * @return
     *     possible object is
     *     {@link Incasso }
     *     
     */
    public Incasso getIncasso() {
        return this.incasso;
    }

    /**
     * Imposta il valore della proprietà incasso.
     * 
     * @param value
     *     allowed object is
     *     {@link Incasso }
     *     
     */
    public void setIncasso(Incasso value) {
        this.incasso = value;
    }

}
