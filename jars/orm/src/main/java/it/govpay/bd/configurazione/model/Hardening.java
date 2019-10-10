package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Hardening implements Serializable{

	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED = "it.govpay.autorizzazione.utenzaAnonima.hardening.enabled";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.serverURL";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.siteKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.secretKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.soglia";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.responseParameter";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_DENY_ON_FAIL = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.denyOnFail";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_READ_TIMEOUT = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.readTimeout";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_CONNECTION_TIMEOUT = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.connectionTimeout";

	private static final long serialVersionUID = 1L;

	private boolean abilitato;
	private GoogleCaptcha googleCatpcha;

	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public GoogleCaptcha getGoogleCatpcha() {
		return googleCatpcha;
	}
	public void setGoogleCatpcha(GoogleCaptcha googleCatpcha) {
		this.googleCatpcha = googleCatpcha;
	}

	public static class GoogleCaptcha {

		private String serverURL;
		private String siteKey;
		private String secretKey;
		private double soglia;
		private String responseParameter;
		private boolean denyOnFail = true;
		private int connectionTimeout;
		private int readTimeout;
		
		public boolean isDenyOnFail() {
			return denyOnFail;
		}
		public void setDenyOnFail(boolean denyOnFail) {
			this.denyOnFail = denyOnFail;
		}
		public int getConnectionTimeout() {
			return connectionTimeout;
		}
		public void setConnectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
		}
		public int getReadTimeout() {
			return readTimeout;
		}
		public void setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
		}
		public String getServerURL() {
			return serverURL;
		}
		public void setServerURL(String serverURL) {
			this.serverURL = serverURL;
		}
		public String getSiteKey() {
			return siteKey;
		}
		public void setSiteKey(String siteKey) {
			this.siteKey = siteKey;
		}
		public String getSecretKey() {
			return secretKey;
		}
		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
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
}
