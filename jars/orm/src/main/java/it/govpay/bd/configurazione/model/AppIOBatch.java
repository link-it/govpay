package it.govpay.bd.configurazione.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class AppIOBatch implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private boolean abilitato;
	private String url = null;
	private BigDecimal timeToLive;
	
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
	public BigDecimal getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(BigDecimal timeToLive) {
		this.timeToLive = timeToLive;
	}
}
