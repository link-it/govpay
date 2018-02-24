package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Stazione;

public class GetStazioneDTOResponse {
	
	private Stazione stazione;
	
	public GetStazioneDTOResponse(Stazione stazione) {
		this.stazione = stazione;
	}

	public Stazione getStazione() {
		return stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

}
