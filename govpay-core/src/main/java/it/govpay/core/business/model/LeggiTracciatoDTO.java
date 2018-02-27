package it.govpay.core.business.model;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;

public class LeggiTracciatoDTO {

	private long id;
	private Applicazione applicazione;
	private Operatore operatore;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Applicazione getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
}
