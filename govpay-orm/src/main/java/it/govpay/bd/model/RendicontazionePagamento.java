package it.govpay.bd.model;

import it.govpay.model.BasicModel;
import it.govpay.model.Rendicontazione;

public class RendicontazionePagamento extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	private transient Fr fr;
	private transient Rendicontazione rendicontazione;
	private transient Pagamento pagamento;
	private transient Versamento versamento;
	private transient SingoloVersamento singoloVersamento;
	private transient String tipo;
	
	public Fr getFr() {
		return fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
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
	public Rendicontazione getRendicontazione() {
		return rendicontazione;
	}
	public void setRendicontazione(Rendicontazione rendicontazione) {
		this.rendicontazione = rendicontazione;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
