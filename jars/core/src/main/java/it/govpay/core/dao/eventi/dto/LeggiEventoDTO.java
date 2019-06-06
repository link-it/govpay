package it.govpay.core.dao.eventi.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

public class LeggiEventoDTO extends BasicCreateRequestDTO {

	private Long id;

	public LeggiEventoDTO(Authentication user, Long id) {
		super(user);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
