
package it.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per nodoInviaCarrelloRPTRisposta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nodoInviaCarrelloRPTRisposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.pagamenti.telematici.gov/}risposta"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="esitoComplessivoOperazione" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listaErroriRPT" type="{http://ws.pagamenti.telematici.gov/}listaErroriRPT" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodoInviaCarrelloRPTRisposta", propOrder = {
    "esitoComplessivoOperazione",
    "url",
    "listaErroriRPT"
})
public class NodoInviaCarrelloRPTRisposta
    extends Risposta
{

    @XmlElement(required = true)
    protected String esitoComplessivoOperazione;
    @XmlElement(defaultValue = "")
    protected String url;
    protected ListaErroriRPT listaErroriRPT;

    /**
     * Recupera il valore della proprietà esitoComplessivoOperazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitoComplessivoOperazione() {
        return this.esitoComplessivoOperazione;
    }

    /**
     * Imposta il valore della proprietà esitoComplessivoOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsitoComplessivoOperazione(String value) {
        this.esitoComplessivoOperazione = value;
    }

    /**
     * Recupera il valore della proprietà url.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Imposta il valore della proprietà url.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Recupera il valore della proprietà listaErroriRPT.
     * 
     * @return
     *     possible object is
     *     {@link ListaErroriRPT }
     *     
     */
    public ListaErroriRPT getListaErroriRPT() {
        return this.listaErroriRPT;
    }

    /**
     * Imposta il valore della proprietà listaErroriRPT.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaErroriRPT }
     *     
     */
    public void setListaErroriRPT(ListaErroriRPT value) {
        this.listaErroriRPT = value;
    }

}
