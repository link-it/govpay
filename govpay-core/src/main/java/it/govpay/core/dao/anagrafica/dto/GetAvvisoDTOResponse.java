package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.avvisi.AvvisoPagamento;

public class GetAvvisoDTOResponse {
	
	private AvvisoPagamento avviso;
	
	public GetAvvisoDTOResponse(AvvisoPagamento avviso) {
		this.avviso = avviso;
	}

	public AvvisoPagamento getIbanAccredito() {
		return this.avviso;
	}

}
