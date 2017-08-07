package it.govpay.core.business.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetIbanDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String codIbanAccredito;
	
	public GetIbanDTO(IAutorizzato user, String codDominio, String codIbanAccredito) {
		super(user);
		this.codDominio = codDominio;
		this.codIbanAccredito = codIbanAccredito;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public String getCodIbanAccredito() {
		return codIbanAccredito;
	}

}
