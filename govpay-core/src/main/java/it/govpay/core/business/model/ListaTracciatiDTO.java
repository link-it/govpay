package it.govpay.core.business.model;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.model.Tracciato.StatoTracciatoType;

import java.util.Date;

public class ListaTracciatiDTO {
	
	private Applicazione applicazione; // filtro applicazione proprietaria del tracciato
	private Operatore operatore; // filtro operatore proprietaria del tracciato
	private Date inizio; // filtro Data caricamento
	private Date fine; // Data caricamento
	private int offset;
	private int limit;
	private StatoTracciatoType stato; //filtro stato
	
	public Date getInizio() {
		return inizio;
	}
	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}
	public Date getFine() {
		return fine;
	}
	public void setFine(Date fine) {
		this.fine = fine;
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
	public Applicazione getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public StatoTracciatoType getStato() {
		return stato;
	}
	public void setStato(StatoTracciatoType stato) {
		this.stato = stato;
	}
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

}
