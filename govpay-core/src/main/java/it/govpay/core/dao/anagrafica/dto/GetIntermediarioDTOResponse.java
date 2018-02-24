package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.Intermediario;

public class GetIntermediarioDTOResponse {
	
	private Intermediario intermediario;
	
	public GetIntermediarioDTOResponse(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

	public Intermediario getIntermediario() {
		return intermediario;
	}

	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

}
