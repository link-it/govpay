package it.govpay.core.dao.configurazione.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Configurazione;
import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

public class PutConfigurazioneDTO extends BasicCreateRequestDTO {

	private Configurazione configurazione;
	private Giornale giornale;

	public PutConfigurazioneDTO(Authentication user) {
		super(user);
	}
	
	
	public Configurazione getConfigurazione() {
		return configurazione;
	}

	public void setConfigurazione(Configurazione configurazione) {
		this.configurazione = configurazione;
	}


	public Giornale getGiornale() {
		return giornale;
	}


	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}
}
