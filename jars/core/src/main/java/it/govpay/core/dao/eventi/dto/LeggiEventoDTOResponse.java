package it.govpay.core.dao.eventi.dto;

import it.govpay.bd.model.Evento;

public class LeggiEventoDTOResponse {
	
	private Evento evento;

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

}
