package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class SslConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean abilitato;
	private String type = null;
	private boolean hostnameVerifier;
	private KeyStore trustStore = null;
	private KeyStore keyStore = null;
	
	
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isHostnameVerifier() {
		return hostnameVerifier;
	}
	public void setHostnameVerifier(boolean hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}
	public KeyStore getTrustStore() {
		return trustStore;
	}
	public void setTrustStore(KeyStore trustStore) {
		this.trustStore = trustStore;
	}
	public KeyStore getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}
	
	
}
