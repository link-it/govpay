package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Applicazione;

public class GetApplicazioneDTOResponse {
	
	private Applicazione applicazione;
	
	public GetApplicazioneDTOResponse(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public Applicazione getApplicazione() {
		return applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

}
