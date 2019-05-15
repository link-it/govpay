package it.govpay.core.business.model;

import it.govpay.bd.model.Versamento;

public class ListaAvvisiDTO {

	private Versamento versamento;
	private int offset;
	private int limit;

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
}

