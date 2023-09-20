/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.model;

public class Connettore extends Versionabile {
	
	private static final long serialVersionUID = 1L;
	public static final String P_TIPOAUTENTICAZIONE_NAME = "TIPOAUTENTICAZIONE";
	public static final String P_TIPOSSL_NAME = "TIPOSSL";
	public static final String P_SSLKSTYPE_NAME = "SSLKSTYPE";
	public static final String P_SSLKSLOCATION_NAME = "SSLKSLOCATION";
	public static final String P_SSLKSPASS_WORD_NAME = "SSLKSPASSWD";
	public static final String P_SSLPKEYPASS_WORD_NAME = "SSLPKEYPASSWD";
	public static final String P_SSLTSTYPE_NAME = "SSLTSTYPE";
	public static final String P_SSLTSLOCATION_NAME = "SSLTSLOCATION";
	public static final String P_SSLTSPASS_WORD_NAME = "SSLTSPASSWD";
	public static final String P_SSLTYPE_NAME = "SSLTYPE";
	public static final String P_HTTPUSER_NAME = "HTTPUSER";
	public static final String P_HTTPPASSW_NAME = "HTTPPASSW";
	public static final String P_URL_NAME = "URL"; // utilizzato da applicazioni e da intermediario per il servizio RPT
	public static final String P_URL_SERVIZI_AVVISATURA_NAME = "URLAVVISI";
	public static final String P_AZIONEINURL_NAME = "AZIONEINURL";
    public static final String P_VERSIONE = "VERSIONE";
    public static final String P_SUBSCRIPTION_KEY_VALUE = "SUBSCRIPTION_KEY_VALUE";
	public static final String P_HTTP_HEADER_AUTH_HEADER_NAME_NAME = "HTTP_HEADER_AUTH_HEADER_NAME";
	public static final String P_HTTP_HEADER_AUTH_HEADER_VALUE_NAME = "HTTP_HEADER_AUTH_HEADER_VALUE";
	
	public enum EnumAuthType {
		SSL, HTTPBasic, HTTP_HEADER, NONE
	}
	
	public enum EnumSslType {
		CLIENT, SERVER
	}
	
	public enum Tipo {
		SOAP, REST;
	}

	private String idConnettore;
	private EnumAuthType tipoAutenticazione;
	private EnumSslType tipoSsl;
	private String sslKsType;
	private String sslKsLocation;
	private String sslKsPasswd;
	private String sslPKeyPasswd;
	private String sslTsType;
	private String sslTsLocation;
	private String sslTsPasswd;
	private String sslType;
	private String httpUser;
	private String httpPassw;
	private String url;
	private String urlServiziAvvisatura;
	private boolean azioneInUrl;
	private String subscriptionKeyValue;
	private String httpHeaderName;
	private String httpHeaderValue;
	
	public Connettore() {
	}
	
	public Connettore(Connettore src) {
		this.azioneInUrl = src.azioneInUrl;
		this.httpPassw = src.httpPassw;
		this.httpUser = src.httpUser;
		this.idConnettore = src.idConnettore;
		this.sslKsLocation = src.sslKsLocation;
		this.sslKsPasswd = src.sslKsPasswd;
		this.sslKsType = src.sslKsType;
		this.sslPKeyPasswd = src.sslPKeyPasswd;
		this.sslTsLocation = src.sslTsLocation;
		this.sslTsPasswd = src.sslTsPasswd;
		this.sslTsType = src.sslTsType;
		this.sslType = src.sslType;
		this.subscriptionKeyValue = src.subscriptionKeyValue;
		this.tipoAutenticazione = src.tipoAutenticazione;
		this.tipoSsl = src.tipoSsl;
		this.url = src.url;
		this.urlServiziAvvisatura = src.urlServiziAvvisatura;
		this.httpHeaderName = src.httpHeaderName;
		this.httpHeaderValue = src.httpHeaderValue;
	}
		
	public String getIdConnettore() {
		return this.idConnettore;
	}
	public void setIdConnettore(String idConnettore) {
		this.idConnettore = idConnettore;
	}
	public EnumAuthType getTipoAutenticazione() {
		return this.tipoAutenticazione;
	}
	public void setTipoAutenticazione(EnumAuthType tipoAutenticazione) {
		this.tipoAutenticazione = tipoAutenticazione;
	}
	public EnumSslType getTipoSsl() {
		return this.tipoSsl;
	}
	public void setTipoSsl(EnumSslType tipoSsl) {
		this.tipoSsl = tipoSsl;
	}
	public String getSslKsType() {
		return this.sslKsType;
	}
	public void setSslKsType(String sslKsType) {
		this.sslKsType = sslKsType;
	}
	public String getSslKsLocation() {
		return this.sslKsLocation;
	}
	public void setSslKsLocation(String sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}
	public String getSslKsPasswd() {
		return this.sslKsPasswd;
	}
	public void setSslKsPasswd(String sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}
	public String getSslPKeyPasswd() {
		return this.sslPKeyPasswd;
	}
	public void setSslPKeyPasswd(String sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}
	public String getSslTsType() {
		return this.sslTsType;
	}
	public void setSslTsType(String sslTsType) {
		this.sslTsType = sslTsType;
	}
	public String getSslTsLocation() {
		return this.sslTsLocation;
	}
	public void setSslTsLocation(String sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}
	public String getSslTsPasswd() {
		return this.sslTsPasswd;
	}
	public void setSslTsPasswd(String sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}
	public String getSslType() {
		return this.sslType;
	}
	public void setSslType(String sslType) {
		this.sslType = sslType;
	}
	public String getHttpUser() {
		return this.httpUser;
	}
	public void setHttpUser(String httpUser) {
		this.httpUser = httpUser;
	}
	public String getHttpPassw() {
		return this.httpPassw;
	}
	public void setHttpPassw(String httpPassw) {
		this.httpPassw = httpPassw;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAzioneInUrl() {
		return this.azioneInUrl;
	}
	public void setAzioneInUrl(boolean azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}
	public Tipo getTipo() {
		return Tipo.valueOf(super.getVersione().getApi());
	}
	public String getUrlServiziAvvisatura() {
		return urlServiziAvvisatura;
	}
	public void setUrlServiziAvvisatura(String urlServiziAvvisatura) {
		this.urlServiziAvvisatura = urlServiziAvvisatura;
	}
	public String getSubscriptionKeyValue() {
		return subscriptionKeyValue;
	}
	public void setSubscriptionKeyValue(String subscriptionKeyValue) {
		this.subscriptionKeyValue = subscriptionKeyValue;
	}
	public String getHttpHeaderName() {
		return httpHeaderName;
	}
	public void setHttpHeaderName(String httpHeaderName) {
		this.httpHeaderName = httpHeaderName;
	}
	public String getHttpHeaderValue() {
		return httpHeaderValue;
	}
	public void setHttpHeaderValue(String httpHeaderValue) {
		this.httpHeaderValue = httpHeaderValue;
	}
}
