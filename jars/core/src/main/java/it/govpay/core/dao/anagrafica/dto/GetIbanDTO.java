package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetIbanDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String codIbanAccredito;
	
	public GetIbanDTO(Authentication user, String codDominio, String codIbanAccredito) {
		super(user);
		this.codDominio = codDominio;
		this.codIbanAccredito = codIbanAccredito;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public String getCodIbanAccredito() {
		return this.codIbanAccredito;
	}

}
