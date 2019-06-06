package it.govpay.core.business.model;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Stampa;

public class ListaAvvisiDTOResponse {

	private List<Stampa> avvisi;
	
	public ListaAvvisiDTOResponse() {
		this.avvisi = new ArrayList<>();
	}

	public List<Stampa> getAvvisi() {
		return this.avvisi;
	}

	public void setAvvisi(List<Stampa> avvisi) {
		this.avvisi = avvisi;
	}
}
