package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetTipoPendenzaDominioDTO extends BasicRequestDTO {
	
	private String codTipoVersamento;
	private String codDominio;
	
	public GetTipoPendenzaDominioDTO(Authentication user, String codDominio, String codTipoVersamento) {
		super(user);
		this.codTipoVersamento = codTipoVersamento;
		this.codDominio = codDominio;
	}

	public String getCodTipoVersamento() {
		return this.codTipoVersamento;
	}

	public String getCodDominio() {
		return codDominio;
	}

}
