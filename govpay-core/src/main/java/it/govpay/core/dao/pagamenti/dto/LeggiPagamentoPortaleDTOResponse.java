package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.PagamentoPortale;

public class LeggiPagamentoPortaleDTOResponse {

	private PagamentoPortale pagamento = null;

	public PagamentoPortale getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoPortale pagamento) {
		this.pagamento = pagamento;
	}
	
}
