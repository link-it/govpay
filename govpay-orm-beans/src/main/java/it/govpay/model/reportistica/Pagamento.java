package it.govpay.model.reportistica;

import java.math.BigDecimal;

import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;

public class Pagamento extends it.govpay.model.rest.Pagamento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal importoPagato;
	private BigDecimal importoDovuto;
	private StatoVersamento statoVersamento;	
	private String causale;
	// Integrazione CSV
	private String codVersamentoEnte;
	private String codFlussoRendicontazione;
	private StatoSingoloVersamento statoSingoloVersamento;
	// dalla tabella FR.
	private String codBicRiversamento;
	private String idRegolamento;
	
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public BigDecimal getImportoDovuto() {
		return importoDovuto;
	}
	public void setImportoDovuto(BigDecimal importoDovuto) {
		this.importoDovuto = importoDovuto;
	}
	public StatoVersamento getStatoVersamento() {
		return statoVersamento;
	}
	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public String getCodFlussoRendicontazione() {
		return codFlussoRendicontazione;
	}
	public void setCodFlussoRendicontazione(String codFlussoRendicontazione) {
		this.codFlussoRendicontazione = codFlussoRendicontazione;
	}
	public String getCodBicRiversamento() {
		return codBicRiversamento;
	}
	public void setCodBicRiversamento(String codBicRiversamento) {
		this.codBicRiversamento = codBicRiversamento;
	}
	public StatoSingoloVersamento getStatoSingoloVersamento() {
		return statoSingoloVersamento;
	}
	public void setStatoSingoloVersamento(StatoSingoloVersamento statoSingoloVersamento) {
		this.statoSingoloVersamento = statoSingoloVersamento;
	}
	public String getIdRegolamento() {
		return idRegolamento;
	}
	public void setIdRegolamento(String idRegolamento) {
		this.idRegolamento = idRegolamento;
	}
	
}
