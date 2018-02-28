package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetEntrataDTO extends BasicRequestDTO {
	
	private String codTipoTributo;
	
	public GetEntrataDTO(IAutorizzato user, String codTipoTributo) {
		super(user);
		this.codTipoTributo = codTipoTributo;
	}

	public String getCodTipoTributo() {
		return codTipoTributo;
	}

}
