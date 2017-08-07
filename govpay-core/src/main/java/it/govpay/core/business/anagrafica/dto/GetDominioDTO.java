package it.govpay.core.business.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetDominioDTO extends BasicRequestDTO {
	
	public GetDominioDTO(IAutorizzato user, String codDominio) {
		super(user);
		this.codDominio = codDominio;
	}

	private String codDominio;
	
	public String getCodDominio() {
		return codDominio;
	}
	
}
