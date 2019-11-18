package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Mail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipo;
	private boolean allegaPdf;
	private String oggetto;
	private String messaggio;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public boolean isAllegaPdf() {
		return allegaPdf;
	}
	public void setAllegaPdf(boolean allegaPdf) {
		this.allegaPdf = allegaPdf;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}

