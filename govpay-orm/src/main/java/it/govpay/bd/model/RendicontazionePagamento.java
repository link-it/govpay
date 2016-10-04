package it.govpay.bd.model;

import it.govpay.model.BasicModel;

public class RendicontazionePagamento extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	private Fr fr;
	private FrApplicazione frApplicazione;
	private Pagamento pagamento;
	private Versamento versamento;
	private SingoloVersamento singoloVersamento;
	private Rpt rpt;
	
	public Fr getFr() {
		return fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	public FrApplicazione getFrApplicazione() {
		return frApplicazione;
	}
	public void setFrApplicazione(FrApplicazione frApplicazione) {
		this.frApplicazione = frApplicazione;
	}
	public Pagamento getPagamento() {
		return pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public SingoloVersamento getSingoloVersamento() {
		return singoloVersamento;
	}
	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}
	public Rpt getRpt() {
		return rpt;
	}
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}
	

}
