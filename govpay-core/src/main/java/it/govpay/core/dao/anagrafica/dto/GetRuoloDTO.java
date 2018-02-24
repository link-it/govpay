package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetRuoloDTO extends BasicRequestDTO {
	
	public GetRuoloDTO(IAutorizzato user, String codRuolo) {
		super(user);
		this.codRuolo = codRuolo;
	}

	private String codRuolo;
	
	public String getCodRuolo() {
		return codRuolo;
	}
	
}
