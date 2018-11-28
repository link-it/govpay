package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public abstract class BasicCreateRequestDTO extends BasicRequestDTO {
	
	public BasicCreateRequestDTO(Authentication authentication) {
		super(authentication);
	}

}
