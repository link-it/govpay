package it.govpay.model.configurazione;

import java.io.Serializable;

public class PromemoriaScadenza implements Serializable{

	private static final long serialVersionUID = 1L;

	private String tipo;
	private String oggetto;
	private String messaggio;
	private Integer preavviso;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	public Integer getPreavviso() {
		return preavviso;
	}
	public void setPreavviso(Integer preavviso) {
		this.preavviso = preavviso;
	}
}
