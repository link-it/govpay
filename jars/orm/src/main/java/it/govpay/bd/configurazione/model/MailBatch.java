package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class MailBatch implements Serializable{

	public static final String IT_GOVPAY_INVIOPROMEMORIA_ENABLED = "it.govpay.invioPromemoria.enabled";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_HOST = "it.govpay.invioPromemoria.mailServer.host";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_PORT = "it.govpay.invioPromemoria.mailServer.port";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_USERNAME = "it.govpay.invioPromemoria.mailServer.username";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_PASSWORD = "it.govpay.invioPromemoria.mailServer.password";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_FROM = "it.govpay.invioPromemoria.mailServer.from";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_READTIMEOUT = "it.govpay.invioPromemoria.mailServer.readTimeout";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_CONNECTIONTIMEOUT = "it.govpay.invioPromemoria.mailServer.connectionTimeout";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean abilitato;
	private MailServer mailserver;

	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public MailServer getMailserver() {
		return mailserver;
	}
	public void setMailserver(MailServer mailserver) {
		this.mailserver = mailserver;
	}
}
