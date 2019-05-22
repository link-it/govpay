package it.govpay.core.dao.configurazione.dto;

import it.govpay.bd.model.Configurazione;

public class LeggiConfigurazioneDTOResponse {
	
	public LeggiConfigurazioneDTOResponse(Configurazione configurazione) {
		this.configurazione = configurazione;
	}

	private Configurazione configurazione;

	public Configurazione getConfigurazione() {
		return configurazione;
	}
}
