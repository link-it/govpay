package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class TracciatoCsv implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String tipo;
	private String intestazione;
	private String richiesta;
	private String risposta;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIntestazione() {
		return intestazione;
	}
	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	
}
