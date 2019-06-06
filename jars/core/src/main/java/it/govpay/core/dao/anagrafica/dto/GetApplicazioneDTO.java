package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetApplicazioneDTO extends BasicRequestDTO {
	
	public GetApplicazioneDTO(Authentication user, String codApplicazione) {
		super(user);
		this.codApplicazione = codApplicazione;
	}

	private String codApplicazione;
	
	public String getCodApplicazione() {
		return this.codApplicazione;
	}
	
}
