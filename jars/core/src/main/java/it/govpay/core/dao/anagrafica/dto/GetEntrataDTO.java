package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetEntrataDTO extends BasicRequestDTO {
	
	private String codTipoTributo;
	
	public GetEntrataDTO(Authentication user, String codTipoTributo) {
		super(user);
		this.codTipoTributo = codTipoTributo;
	}

	public String getCodTipoTributo() {
		return this.codTipoTributo;
	}

}
