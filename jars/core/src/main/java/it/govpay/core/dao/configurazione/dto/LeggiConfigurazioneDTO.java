package it.govpay.core.dao.configurazione.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;

public class LeggiConfigurazioneDTO  extends BasicFindRequestDTO {
	
	public LeggiConfigurazioneDTO(Authentication user) {
		super(user);
	}

}
