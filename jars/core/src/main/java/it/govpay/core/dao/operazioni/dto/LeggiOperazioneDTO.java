package it.govpay.core.dao.operazioni.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;

public class LeggiOperazioneDTO extends BasicFindRequestDTO {
	
	public LeggiOperazioneDTO(Authentication user) {
		super(user);
	}
	
	private String idOperazione = null;
	
	public String getIdOperazione() {
		return this.idOperazione;
	}

	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}

}
