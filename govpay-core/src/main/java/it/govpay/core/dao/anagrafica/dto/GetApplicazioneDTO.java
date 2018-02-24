package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetApplicazioneDTO extends BasicRequestDTO {
	
	public GetApplicazioneDTO(IAutorizzato user, String codApplicazione) {
		super(user);
		this.codApplicazione = codApplicazione;
	}

	private String codApplicazione;
	
	public String getCodApplicazione() {
		return codApplicazione;
	}
	
}
