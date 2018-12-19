package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class BasicRequestDTO {
	
	public BasicRequestDTO(Authentication authentication) {
		this.authentication = authentication;
	}

	private Authentication authentication;
	
	// TODO Rinominare
	public Authentication getUser() {
		return this.authentication;
	}
}
