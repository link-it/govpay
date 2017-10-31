package it.govpay.core.business.model;

import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;

public class ListaAvvisiDTO {

	private StatoAvviso stato;
	private int offset;
	private int limit;

	public StatoAvviso getStato() {
		return stato;
	}

	public void setStato(StatoAvviso stato) {
		this.stato = stato;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}

