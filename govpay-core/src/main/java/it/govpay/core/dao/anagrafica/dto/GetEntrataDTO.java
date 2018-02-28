package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetEntrataDTO extends BasicRequestDTO {
	
	private String codTributo;
	
	public GetEntrataDTO(IAutorizzato user, String codTributo) {
		super(user);
		this.codTributo = codTributo;
	}

	public String getCodTributo() {
		return codTributo;
	}

}
