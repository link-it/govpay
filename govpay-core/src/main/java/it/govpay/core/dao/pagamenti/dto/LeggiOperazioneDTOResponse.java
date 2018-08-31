package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Operazione;

public class LeggiOperazioneDTOResponse {

	private Operazione operazione;

	public Operazione getOperazione() {
		return operazione;
	}

	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}
	
}
