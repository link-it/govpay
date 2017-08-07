package it.govpay.core.business.anagrafica.dto;

import it.govpay.bd.model.Dominio;

public class GetDominioDTOResponse {
	
	private Dominio dominio;
	
	public GetDominioDTOResponse(Dominio dominio) {
		this.dominio = dominio;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

}
