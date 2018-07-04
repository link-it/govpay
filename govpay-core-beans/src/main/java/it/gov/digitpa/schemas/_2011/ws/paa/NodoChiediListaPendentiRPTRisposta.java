
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoChiediListaPendentiRPTRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoChiediListaPendentiRPTRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="listaRPTPendenti" type="{http://ws.pagamenti.telematici.gov/}tipoListaRPTPendenti" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoChiediListaPendentiRPTRisposta", propOrder = {
    "listaRPTPendenti"
})
public class NodoChiediListaPendentiRPTRisposta
    extends Risposta
{

    protected TipoListaRPTPendenti listaRPTPendenti;

    /**
     * Recupera il valore della proprietà listaRPTPendenti.
     * 
     * @return
     *     possible object is
     *     {@link TipoListaRPTPendenti }
     *     
     */
    public TipoListaRPTPendenti getListaRPTPendenti() {
        return listaRPTPendenti;
    }

    /**
     * Imposta il valore della proprietà listaRPTPendenti.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoListaRPTPendenti }
     *     
     */
    public void setListaRPTPendenti(TipoListaRPTPendenti value) {
        this.listaRPTPendenti = value;
    }

}
