package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.Ruolo;

public class GetRuoloDTOResponse {
	
	private Ruolo ruolo;
	
	public GetRuoloDTOResponse(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

}
