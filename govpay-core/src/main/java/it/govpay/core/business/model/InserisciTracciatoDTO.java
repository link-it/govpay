package it.govpay.core.business.model;

import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;

public class InserisciTracciatoDTO {
	
	private byte[] tracciato;
	private String nomeTracciato;
	private Operatore operatore;
	private Applicazione applicazione;
	
	public byte[] getTracciato() {
		return tracciato;
	}
	public void setTracciato(byte[] tracciato) {
		this.tracciato = tracciato;
	}
	public String getNomeTracciato() {
		return nomeTracciato;
	}
	public void setNomeTracciato(String nomeTracciato) {
		this.nomeTracciato = nomeTracciato;
	}
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public Applicazione getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

}
