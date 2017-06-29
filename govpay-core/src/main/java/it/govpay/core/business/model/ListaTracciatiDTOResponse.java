package it.govpay.core.business.model;

import java.util.List;

import it.govpay.bd.model.Tracciato;

public class ListaTracciatiDTOResponse {

	private List<Tracciato> tracciati;
	
	public List<Tracciato> getTracciati() {
		return tracciati;
	}

	public void setTracciati(List<Tracciato> tracciati) {
		this.tracciati = tracciati;
	}

}
