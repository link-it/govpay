package it.govpay.bd.model.reportistica;

import java.math.BigDecimal;

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
	
}
