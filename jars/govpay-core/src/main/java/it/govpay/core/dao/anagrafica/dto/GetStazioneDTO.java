package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetStazioneDTO extends BasicRequestDTO {
	
	public GetStazioneDTO(IAutorizzato user, String codIntermediario, String codStazione) {
		super(user);
		this.codIntermediario = codIntermediario;
		this.codStazione = codStazione;
	}

	private String codStazione;
	
	public String getCodStazione() {
		return this.codStazione;
	}
	
	private String codIntermediario;
	
	public String getCodIntermediario() {
		return this.codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	
}
