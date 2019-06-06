package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetDominioDTO extends BasicRequestDTO {
	
	public GetDominioDTO(Authentication user, String codDominio) {
		super(user);
		this.codDominio = codDominio;
	}

	private String codDominio;
	
	public String getCodDominio() {
		return this.codDominio;
	}
	
}
