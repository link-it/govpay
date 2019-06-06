package it.govpay.core.business.model;

import it.govpay.model.Stampa;

public class PrintAvvisoDTOResponse {

	private Stampa avviso;

	public Stampa getAvviso() {
		return this.avviso;
	}

	public void setAvviso(Stampa avviso) {
		this.avviso = avviso;
	}
}
