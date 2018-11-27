package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;

public class GetStazioneDTOResponse {
	
	private Stazione stazione;
	
	private List<Dominio>  domini;
	
	public List<Dominio> getDomini() {
		return domini;
	}

	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}
	
	public GetStazioneDTOResponse(Stazione stazione) {
		this.stazione = stazione;
	}

	public Stazione getStazione() {
		return this.stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

}
