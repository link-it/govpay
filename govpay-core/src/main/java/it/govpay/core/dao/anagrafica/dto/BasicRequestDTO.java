package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class BasicRequestDTO {
	
	public BasicRequestDTO(IAutorizzato user) {
		this.user = user;
	}

	private IAutorizzato user;
	
	public IAutorizzato getUser() {
		return this.user;
	}
}
