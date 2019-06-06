package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetUnitaOperativaDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String codUnivocoUnitaOperativa;
	
	public GetUnitaOperativaDTO(Authentication user, String codDominio, String codUnivocoUnitaOperativa) {
		super(user);
		this.codDominio = codDominio;
		this.codUnivocoUnitaOperativa = codUnivocoUnitaOperativa;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public String getCodUnivocoUnitaOperativa() {
		return this.codUnivocoUnitaOperativa;
	}

}
