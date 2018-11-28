package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetTributoDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String codTributo;
	
	public GetTributoDTO(Authentication user, String codDominio, String codTributo) {
		super(user);
		this.codDominio = codDominio;
		this.codTributo = codTributo;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public String getCodTributo() {
		return this.codTributo;
	}

}
