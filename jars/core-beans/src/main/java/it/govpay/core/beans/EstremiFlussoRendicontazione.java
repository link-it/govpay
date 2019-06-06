
package it.govpay.core.beans;

import java.math.BigDecimal;
import java.util.Date;

public class EstremiFlussoRendicontazione {

    protected String codFlusso;
    protected String codBicRiversamento;
    protected Integer annoRiferimento;
    protected String codPsp;
    protected Date dataFlusso;
    protected Date dataRegolamento;
    protected String iur;
    protected Long numeroPagamenti;
    protected BigDecimal importoTotale;

    /**
     * Recupera il valore della proprietà codFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFlusso() {
        return this.codFlusso;
    }

    /**
     * Imposta il valore della proprietà codFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFlusso(String value) {
        this.codFlusso = value;
    }

    /**
     * Recupera il valore della proprietà codBicRiversamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBicRiversamento() {
        return this.codBicRiversamento;
    }

    /**
     * Imposta il valore della proprietà codBicRiversamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBicRiversamento(String value) {
        this.codBicRiversamento = value;
    }

    /**
     * Recupera il valore della proprietà annoRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAnnoRiferimento() {
        return this.annoRiferimento;
    }

    /**
     * Imposta il valore della proprietà annoRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoRiferimento(Integer value) {
        this.annoRiferimento = value;
    }

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
     * Recupera il valore della proprietà dataFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataFlusso() {
        return this.dataFlusso;
    }

    /**
     * Imposta il valore della proprietà dataFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFlusso(Date value) {
        this.dataFlusso = value;
    }

    /**
     * Recupera il valore della proprietà dataRegolamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataRegolamento() {
        return this.dataRegolamento;
    }

    /**
     * Imposta il valore della proprietà dataRegolamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataRegolamento(Date value) {
        this.dataRegolamento = value;
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
     * Recupera il valore della proprietà numeroPagamenti.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroPagamenti() {
        return this.numeroPagamenti;
    }

    /**
     * Imposta il valore della proprietà numeroPagamenti.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumeroPagamenti(Long value) {
        this.numeroPagamenti = value;
    }

    /**
     * Recupera il valore della proprietà importoTotale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoTotale() {
        return this.importoTotale;
    }

    /**
     * Imposta il valore della proprietà importoTotale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoTotale(BigDecimal value) {
        this.importoTotale = value;
    }

}
