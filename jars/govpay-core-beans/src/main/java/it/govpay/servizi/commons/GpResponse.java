
package it.govpay.servizi.commons;

public class GpResponse {

    protected String codOperazione;
    protected Mittente mittente;
    protected String codEsito;
    protected String descrizioneEsito;
    protected String dettaglioEsito;

    /**
     * Recupera il valore della proprietà codOperazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOperazione() {
        return this.codOperazione;
    }

    /**
     * Imposta il valore della proprietà codOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOperazione(String value) {
        this.codOperazione = value;
    }

    /**
     * Recupera il valore della proprietà mittente.
     * 
     * @return
     *     possible object is
     *     {@link Mittente }
     *     
     */
    public Mittente getMittente() {
        return this.mittente;
    }

    /**
     * Imposta il valore della proprietà mittente.
     * 
     * @param value
     *     allowed object is
     *     {@link Mittente }
     *     
     */
    public void setMittente(Mittente value) {
        this.mittente = value;
    }

    /**
     * Recupera il valore della proprietà codEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEsito() {
        return this.codEsito;
    }

    /**
     * Imposta il valore della proprietà codEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEsito(String value) {
        this.codEsito = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneEsito() {
        return this.descrizioneEsito;
    }

    /**
     * Imposta il valore della proprietà descrizioneEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneEsito(String value) {
        this.descrizioneEsito = value;
    }

    /**
     * Recupera il valore della proprietà dettaglioEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDettaglioEsito() {
        return this.dettaglioEsito;
    }

    /**
     * Imposta il valore della proprietà dettaglioEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDettaglioEsito(String value) {
        this.dettaglioEsito = value;
    }

}
