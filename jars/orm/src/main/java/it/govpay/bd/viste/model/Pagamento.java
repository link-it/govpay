package it.govpay.bd.viste.model;

import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.BasicModel;

public class Pagamento extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private it.govpay.bd.model.Pagamento pagamento;
	private SingoloVersamento singoloVersamento;
	private Versamento versamento;
	private Rpt rpt;
	private Incasso incasso;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public it.govpay.bd.model.Pagamento getPagamento() {
		return pagamento;
	}
	public void setPagamento(it.govpay.bd.model.Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public SingoloVersamento getSingoloVersamento() {
		return singoloVersamento;
	}
	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}
	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public Rpt getRpt() {
		return rpt;
	}
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}
	public Incasso getIncasso() {
		return incasso;
	}
	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
	}
}
