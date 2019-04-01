package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class BasicRequestDTO {
	
	private boolean ereditaAutorizzazione;
	
	public BasicRequestDTO(Authentication authentication) {
		this.authentication = authentication;
		this.ereditaAutorizzazione = false;
	}

	private Authentication authentication;
	
	public Authentication getUser() {
		return this.authentication;
	}

	public boolean isEreditaAutorizzazione() {
		return ereditaAutorizzazione;
	}

	public void setEreditaAutorizzazione(boolean ereditaAutorizzazione) {
		this.ereditaAutorizzazione = ereditaAutorizzazione;
	}
	
	
}
