package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.PagamentoPortale;

public class LeggiPagamentoPortaleDTOResponse {

	private PagamentoPortale pagamento = null;
	private List<LeggiRptDTOResponse> listaRpp = null;
	private List<LeggiPendenzaDTOResponse> listaPendenze = null;

	public PagamentoPortale getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(PagamentoPortale pagamento) {
		this.pagamento = pagamento;
	}

	public List<LeggiRptDTOResponse> getListaRpp() {
		return this.listaRpp;
	}

	public void setListaRpp(List<LeggiRptDTOResponse> listaRpp) {
		this.listaRpp = listaRpp;
	}

	public List<LeggiPendenzaDTOResponse> getListaPendenze() {
		return this.listaPendenze;
	}

	public void setListaPendenze(List<LeggiPendenzaDTOResponse> listaPendenze) {
		this.listaPendenze = listaPendenze;
	}
	
}
