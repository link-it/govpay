
package it.govpay.servizi.commons;

public class Canale {

    protected String codPsp;
    protected String codCanale;
    protected TipoVersamento tipoVersamento;

    /**
     * Recupera il valore della proprietà codPsp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPsp() {
        return this.codPsp;
    }

    /**
     * Imposta il valore della proprietà codPsp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPsp(String value) {
        this.codPsp = value;
    }

    /**
     * Recupera il valore della proprietà codCanale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCanale() {
        return this.codCanale;
    }

    /**
     * Imposta il valore della proprietà codCanale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCanale(String value) {
        this.codCanale = value;
    }

    /**
     * Recupera il valore della proprietà tipoVersamento.
     * 
     * @return
     *     possible object is
     *     {@link TipoVersamento }
     *     
     */
    public TipoVersamento getTipoVersamento() {
        return this.tipoVersamento;
    }

    /**
     * Imposta il valore della proprietà tipoVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoVersamento }
     *     
     */
    public void setTipoVersamento(TipoVersamento value) {
        this.tipoVersamento = value;
    }

}
