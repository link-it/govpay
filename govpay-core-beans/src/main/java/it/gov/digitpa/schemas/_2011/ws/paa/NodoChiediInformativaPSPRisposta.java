
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediInformativaPSPRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediInformativaPSPRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xmlInformativa" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediInformativaPSPRisposta", propOrder = {
    "xmlInformativa"
})
public class NodoChiediInformativaPSPRisposta
    extends Risposta
{

    @XmlMimeType("application/octet-stream")
    protected DataHandler xmlInformativa;

    /**
     * Recupera il valore della proprietà xmlInformativa.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getXmlInformativa() {
        return this.xmlInformativa;
    }

    /**
     * Imposta il valore della proprietà xmlInformativa.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setXmlInformativa(DataHandler value) {
        this.xmlInformativa = value;
    }

}
