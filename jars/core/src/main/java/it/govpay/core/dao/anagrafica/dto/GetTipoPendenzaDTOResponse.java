package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.TipoVersamento;

public class GetTipoPendenzaDTOResponse {
	
	private TipoVersamento tipoVersamento;
	
	public GetTipoPendenzaDTOResponse(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public TipoVersamento getTipoVersamento() {
		return this.tipoVersamento;
	}

}
