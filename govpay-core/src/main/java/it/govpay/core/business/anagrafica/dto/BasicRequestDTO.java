package it.govpay.core.business.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class BasicRequestDTO {
	
	public BasicRequestDTO(IAutorizzato user) {
		this.user = user;
	}

	private IAutorizzato user;
	
	public IAutorizzato getUser() {
		return user;
	}
}
