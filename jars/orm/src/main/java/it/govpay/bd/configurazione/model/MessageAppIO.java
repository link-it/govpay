package it.govpay.bd.configurazione.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class MessageAppIO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal timeToLive;
	private String tipo;
	private String subject;
	private String body;
	
	public BigDecimal getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(BigDecimal timeToLive) {
		this.timeToLive = timeToLive;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
