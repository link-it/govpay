package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetIntermediarioDTO extends BasicRequestDTO {
	
	public GetIntermediarioDTO(IAutorizzato user, String codIntermediario) {
		super(user);
		this.codIntermediario = codIntermediario;
	}

	private String codIntermediario;
	
	public String getCodIntermediario() {
		return codIntermediario;
	}
	
}
