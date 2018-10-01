package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public abstract class BasicCreateRequestDTO extends BasicRequestDTO {
	
	public BasicCreateRequestDTO(IAutorizzato user) {
		super(user);
	}

}
