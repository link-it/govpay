package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetStazioneDTO extends BasicRequestDTO {
	
	public GetStazioneDTO(IAutorizzato user, String codStazione) {
		super(user);
		this.codStazione = codStazione;
	}

	private String codStazione;
	
	public String getCodStazione() {
		return codStazione;
	}
	
	private String codIntermediario;
	
	public String getCodIntermediario() {
		return codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	
}
