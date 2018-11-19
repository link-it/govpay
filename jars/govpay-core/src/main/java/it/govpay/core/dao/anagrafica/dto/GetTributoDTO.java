package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetTributoDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String codTributo;
	
	public GetTributoDTO(IAutorizzato user, String codDominio, String codTributo) {
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
