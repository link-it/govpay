package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class AppIO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private boolean abilitato;
	private String url = null;
	private MessageAppIO message= null;
	
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public MessageAppIO getMessage() {
		return message;
	}
	public void setMessage(MessageAppIO message) {
		this.message = message;
	}
	

}
