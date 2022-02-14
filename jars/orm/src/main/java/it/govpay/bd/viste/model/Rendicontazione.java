package it.govpay.bd.viste.model;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.model.BasicModel;

public class Rendicontazione extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Fr fr;
	private it.govpay.bd.model.Rendicontazione rendicontazione;
	private SingoloVersamento singoloVersamento;
	private Versamento versamento;
	private Pagamento pagamento;
	private Rpt rpt;
	private Incasso incasso;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Fr getFr() {
		return fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	public it.govpay.bd.model.Rendicontazione getRendicontazione() {
		return rendicontazione;
	}
	public void setRendicontazione(it.govpay.bd.model.Rendicontazione rendicontazione) {
		this.rendicontazione = rendicontazione;
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
	public Pagamento getPagamento() {
		return pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
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
