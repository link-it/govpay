package it.govpay.core.business.model;

import it.govpay.bd.loader.model.Operazione;

public class LeggiOperazioneDTOResponse {
	
	private Operazione operazione;

	public Operazione getOperazione() {
		return operazione;
	}

	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}
}
