package it.govpay.core.business.model;

import java.util.List;

import it.govpay.bd.model.Operazione;

public class ListaOperazioniDTOResponse {
	
	private List<Operazione> operazioni;

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}
	
}
