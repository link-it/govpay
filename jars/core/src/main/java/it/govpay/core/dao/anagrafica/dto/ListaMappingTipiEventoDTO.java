package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class ListaMappingTipiEventoDTO extends BasicFindRequestDTO{

	public ListaMappingTipiEventoDTO(Authentication authentication) {
		super(authentication);
	}

}
