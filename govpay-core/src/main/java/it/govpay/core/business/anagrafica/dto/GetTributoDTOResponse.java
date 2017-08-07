package it.govpay.core.business.anagrafica.dto;

import it.govpay.bd.model.Tributo;

public class GetTributoDTOResponse {
	
	private Tributo tributo;
	
	public GetTributoDTOResponse(Tributo tributo) {
		this.tributo = tributo;
	}

	public Tributo getTributo() {
		return tributo;
	}

}
