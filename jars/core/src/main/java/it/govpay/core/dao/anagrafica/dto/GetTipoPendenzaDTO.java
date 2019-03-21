package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetTipoPendenzaDTO extends BasicRequestDTO {
	
	private String codTipoVersamento;
	
	public GetTipoPendenzaDTO(Authentication user, String codTipoVersamento) {
		super(user);
		this.codTipoVersamento = codTipoVersamento;
	}

	public String getCodTipoVersamento() {
		return this.codTipoVersamento;
	}

}
