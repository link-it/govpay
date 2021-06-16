package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;

public class LeggiIncassoDTOResponse {
	
	private Incasso incasso;
	private Fr fr;
	
	public Incasso getIncasso() {
		return this.incasso;
	}

	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
	}

	public Fr getFr() {
		return fr;
	}

	public void setFr(Fr fr) {
		this.fr = fr;
	}

}
