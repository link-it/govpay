package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;

public class Rendicontazione extends it.govpay.model.Rendicontazione {

	private static final long serialVersionUID = 1L;
	
	private Fr fr;
	private Pagamento pagamento;
	private boolean pagamentoDaCreare = false;
	
	public Fr getFr(BasicBD bd) throws ServiceException {
		if(this.fr == null) {
			FrBD frBD = new FrBD(bd);
			this.fr = frBD.getFr(this.getIdFr());
		}
		return this.fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	public Pagamento getPagamento(BasicBD bd) throws ServiceException {
		if(this.pagamento == null && this.getIdPagamento() != null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			this.pagamento = pagamentiBD.getPagamento(this.getIdPagamento());
		}
		return this.pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public boolean isPagamentoDaCreare() {
		return pagamentoDaCreare;
	}
	public void setPagamentoDaCreare(boolean pagamentoDaCreare) {
		this.pagamentoDaCreare = pagamentoDaCreare;
	}
}
