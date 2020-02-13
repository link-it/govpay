package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class GoogleCaptcha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serverURL;
	private String siteKey;
	private String secretKey;
	private double soglia;
	private String responseParameter;
	private boolean denyOnFail;
	private int connectionTimeout;
	private int readTimeout;

	public GoogleCaptcha() {
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
}