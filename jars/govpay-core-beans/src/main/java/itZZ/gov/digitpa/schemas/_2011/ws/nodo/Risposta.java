
package itZZ.gov.digitpa.schemas._2011.ws.nodo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per risposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="risposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fault" type="{http://ws.pagamenti.telematici.gov/}faultBean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "risposta", propOrder = {
    "fault"
})
@XmlSeeAlso({
    EsitoPaaInviaRT.class,
    TipoInviaEsitoStornoRisposta.class
})
public class Risposta {

    protected FaultBean fault;

    /**
     * Recupera il valore della proprietà fault.
     * 
     * @return
     *     possible object is
     *     {@link FaultBean }
     *     
     */
    public FaultBean getFault() {
        return this.fault;
    }

    /**
     * Imposta il valore della proprietà fault.
     * 
     * @param value
     *     allowed object is
     *     {@link FaultBean }
     *     
     */
    public void setFault(FaultBean value) {
        this.fault = value;
    }

}
