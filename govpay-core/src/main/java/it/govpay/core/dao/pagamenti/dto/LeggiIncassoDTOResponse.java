package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Incasso;

public class LeggiIncassoDTOResponse {
	
	private Incasso incasso;

	public Incasso getIncasso() {
		return incasso;
	}

	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
	}

}
