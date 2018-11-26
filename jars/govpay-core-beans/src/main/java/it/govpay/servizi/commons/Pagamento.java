
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.Date;

public class Pagamento {

    protected String codSingoloVersamentoEnte;
    protected BigDecimal importoPagato;
    protected String iur;
    protected Date dataPagamento;
    protected Date dataAcquisizione;
    protected BigDecimal commissioniPsp;
    protected Pagamento.Allegato allegato;
    protected Date dataAcquisizioneRevoca;
    protected String causaleRevoca;
    protected String datiRevoca;
    protected BigDecimal importoRevocato;
    protected String esitoRevoca;
    protected String datiEsitoRevoca;

    /**
     * Recupera il valore della proprietà codSingoloVersamentoEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSingoloVersamentoEnte() {
        return this.codSingoloVersamentoEnte;
    }

    /**
     * Imposta il valore della proprietà codSingoloVersamentoEnte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSingoloVersamentoEnte(String value) {
        this.codSingoloVersamentoEnte = value;
    }

    /**
     * Recupera il valore della proprietà importoPagato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoPagato() {
        return this.importoPagato;
    }

    /**
     * Imposta il valore della proprietà importoPagato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoPagato(BigDecimal value) {
        this.importoPagato = value;
    }

    /**
     * Recupera il valore della proprietà iur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIur() {
        return this.iur;
    }

    /**
     * Imposta il valore della proprietà iur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIur(String value) {
        this.iur = value;
    }

    /**
     * Recupera il valore della proprietà dataPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataPagamento() {
        return this.dataPagamento;
    }

    /**
     * Imposta il valore della proprietà dataPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataPagamento(Date value) {
        this.dataPagamento = value;
    }

    /**
     * Recupera il valore della proprietà dataAcquisizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizione() {
        return this.dataAcquisizione;
    }

    /**
     * Imposta il valore della proprietà dataAcquisizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAcquisizione(Date value) {
        this.dataAcquisizione = value;
    }

    /**
     * Recupera il valore della proprietà commissioniPsp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getCommissioniPsp() {
        return this.commissioniPsp;
    }

    /**
     * Imposta il valore della proprietà commissioniPsp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommissioniPsp(BigDecimal value) {
        this.commissioniPsp = value;
    }

    /**
     * Recupera il valore della proprietà allegato.
     * 
     * @return
     *     possible object is
     *     {@link Pagamento.Allegato }
     *     
     */
    public Pagamento.Allegato getAllegato() {
        return this.allegato;
    }

    /**
     * Imposta il valore della proprietà allegato.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagamento.Allegato }
     *     
     */
    public void setAllegato(Pagamento.Allegato value) {
        this.allegato = value;
    }

    /**
     * Recupera il valore della proprietà dataAcquisizioneRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataAcquisizioneRevoca() {
        return this.dataAcquisizioneRevoca;
    }

    /**
     * Imposta il valore della proprietà dataAcquisizioneRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAcquisizioneRevoca(Date value) {
        this.dataAcquisizioneRevoca = value;
    }

    /**
     * Recupera il valore della proprietà causaleRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleRevoca() {
        return this.causaleRevoca;
    }

    /**
     * Imposta il valore della proprietà causaleRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleRevoca(String value) {
        this.causaleRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiRevoca() {
        return this.datiRevoca;
    }

    /**
     * Imposta il valore della proprietà datiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiRevoca(String value) {
        this.datiRevoca = value;
    }

    /**
     * Recupera il valore della proprietà importoRevocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoRevocato() {
        return this.importoRevocato;
    }

    /**
     * Imposta il valore della proprietà importoRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoRevocato(BigDecimal value) {
        this.importoRevocato = value;
    }

    /**
     * Recupera il valore della proprietà esitoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitoRevoca() {
        return this.esitoRevoca;
    }

    /**
     * Imposta il valore della proprietà esitoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsitoRevoca(String value) {
        this.esitoRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiEsitoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatiEsitoRevoca() {
        return this.datiEsitoRevoca;
    }

    /**
     * Imposta il valore della proprietà datiEsitoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatiEsitoRevoca(String value) {
        this.datiEsitoRevoca = value;
    }


    public static class Allegato {

        protected TipoAllegato tipo;
        protected byte[] testo;

        /**
         * Recupera il valore della proprietà tipo.
         * 
         * @return
         *     possible object is
         *     {@link TipoAllegato }
         *     
         */
        public TipoAllegato getTipo() {
            return this.tipo;
        }

        /**
         * Imposta il valore della proprietà tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoAllegato }
         *     
         */
        public void setTipo(TipoAllegato value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della proprietà testo.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getTesto() {
            return this.testo;
        }

        /**
         * Imposta il valore della proprietà testo.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setTesto(byte[] value) {
            this.testo = value;
        }

    }

}
