package it.govpay.core.dao.eventi.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.utils.EventoContext;

public class PutEventoDTO extends BasicCreateRequestDTO {

	private EventoContext evento;
	
	public PutEventoDTO(Authentication user) {
		super(user);
	}

	public EventoContext getEvento() {
		return evento;
	}

	public void setEvento(EventoContext evento) {
		this.evento = evento;
	}
	
}
