package it.govpay.model.configurazione;

import java.io.Serializable;

public class PromemoriaRicevutaBase implements Serializable{

	private static final long serialVersionUID = 1L;

	private String tipo;
	private String oggetto;
	private String messaggio;
	private boolean soloEseguiti;
	
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
	public boolean isSoloEseguiti() {
		return soloEseguiti;
	}
	public void setSoloEseguiti(boolean soloEseguiti) {
		this.soloEseguiti = soloEseguiti;
	}
	
}
