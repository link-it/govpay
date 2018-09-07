
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediFlussoRendicontazioneRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediFlussoRendicontazioneRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xmlRendicontazione" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediFlussoRendicontazioneRisposta", propOrder = {
    "xmlRendicontazione"
})
public class NodoChiediFlussoRendicontazioneRisposta
    extends Risposta
{

    @XmlMimeType("application/octet-stream")
    protected DataHandler xmlRendicontazione;

    /**
     * Recupera il valore della proprietà xmlRendicontazione.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getXmlRendicontazione() {
        return this.xmlRendicontazione;
    }

    /**
     * Imposta il valore della proprietà xmlRendicontazione.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setXmlRendicontazione(DataHandler value) {
        this.xmlRendicontazione = value;
    }

}
