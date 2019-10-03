package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class ReCaptcha implements Serializable{
	
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_ENABLED = "it.govpay.autenticazione.utenzaAnonima.captcha.enabled";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL = "it.govpay.autenticazione.utenzaAnonima.captcha.serverURL";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY = "it.govpay.autenticazione.utenzaAnonima.captcha.siteKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY = "it.govpay.autenticazione.utenzaAnonima.captcha.secretKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA = "it.govpay.autenticazione.utenzaAnonima.captcha.soglia";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER = "it.govpay.autenticazione.utenzaAnonima.captcha.responseParameter";

	private static final long serialVersionUID = 1L;

	private String serverURL;
	private String site;
	private String secret;
	private boolean abilitato;
	private double soglia;
	private String responseParameter;
	
	public String getServerURL() {
		return serverURL;
	}
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public double getSoglia() {
		return soglia;
	}
	public void setSoglia(double soglia) {
		this.soglia = soglia;
	}
	public String getResponseParameter() {
		return responseParameter;
	}
	public void setResponseParameter(String responseParameter) {
		this.responseParameter = responseParameter;
	}
}
