package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Canale;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Psp;

public class LeggiPagamentoPortaleDTOResponse {

	private PagamentoPortale pagamento = null;
	private Psp psp = null;
	private Canale canale = null;

	public PagamentoPortale getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoPortale pagamento) {
		this.pagamento = pagamento;
	}

	public Psp getPsp() {
		return psp;
	}

	public void setPsp(Psp psp) {
		this.psp = psp;
	}

	public Canale getCanale() {
		return canale;
	}

	public void setCanale(Canale canale) {
		this.canale = canale;
	}
	
}
