package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetIntermediarioDTO extends BasicRequestDTO {
	
	public GetIntermediarioDTO(Authentication user, String codIntermediario) {
		super(user);
		this.codIntermediario = codIntermediario;
	}

	private String codIntermediario;
	
	public String getCodIntermediario() {
		return this.codIntermediario;
	}
	
}
