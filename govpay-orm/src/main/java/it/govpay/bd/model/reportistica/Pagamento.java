package it.govpay.bd.model.reportistica;

import java.math.BigDecimal;

public class Pagamento extends it.govpay.bd.model.rest.Pagamento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal importoPagato;
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
	
}
