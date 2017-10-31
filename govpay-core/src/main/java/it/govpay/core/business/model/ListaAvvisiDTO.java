package it.govpay.core.business.model;

import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;

public class ListaAvvisiDTO {

	private StatoAvviso stato;

	public StatoAvviso getStato() {
		return stato;
	}

	public void setStato(StatoAvviso stato) {
		this.stato = stato;
	}
}
