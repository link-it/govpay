package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.TipoTributo;

public class GetEntrataDTOResponse {
	
	private TipoTributo tipoTributo;
	
	public GetEntrataDTOResponse(TipoTributo tipoTributo) {
		this.tipoTributo = tipoTributo;
	}

	public TipoTributo getTipoTributo() {
		return tipoTributo;
	}

}
