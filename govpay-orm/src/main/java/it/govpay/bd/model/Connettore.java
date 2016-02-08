/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.bd.model;

public class Connettore extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public static final String P_TIPOAUTENTICAZIONE_NAME = "TIPOAUTENTICAZIONE";
	public static final String P_TIPOSSL_NAME = "TIPOSSL";
	public static final String P_SSLKSTYPE_NAME = "SSLKSTYPE";
	public static final String P_SSLKSLOCATION_NAME = "SSLKSLOCATION";
	public static final String P_SSLKSPASSWD_NAME = "SSLKSPASSWD";
	public static final String P_SSLPKEYPASSWD_NAME = "SSLPKEYPASSWD";
	public static final String P_SSLTSTYPE_NAME = "SSLTSTYPE";
	public static final String P_SSLTSLOCATION_NAME = "SSLTSLOCATION";
	public static final String P_SSLTSPASSWD_NAME = "SSLTSPASSWD";
	public static final String P_SSLTYPE_NAME = "SSLTYPE";
	public static final String P_HTTPUSER_NAME = "HTTPUSER";
	public static final String P_HTTPPASSW_NAME = "HTTPPASSW";
	public static final String P_URL_NAME = "URL";
	public static final String P_AZIONEINURL_NAME = "AZIONEINURL";
	
	public enum EnumAuthType {
		SSL, HTTPBasic, NONE
	}
	
	public enum EnumSslType {
		CLIENT, SERVER
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
	private boolean azioneInUrl;
	
	public Connettore() {
		// TODO Auto-generated constructor stub
	}
		
	public String getIdConnettore() {
		return idConnettore;
	}
	public void setIdConnettore(String idConnettore) {
		this.idConnettore = idConnettore;
	}
	public EnumAuthType getTipoAutenticazione() {
		return tipoAutenticazione;
	}
	public void setTipoAutenticazione(EnumAuthType tipoAutenticazione) {
		this.tipoAutenticazione = tipoAutenticazione;
	}
	public EnumSslType getTipoSsl() {
		return tipoSsl;
	}
	public void setTipoSsl(EnumSslType tipoSsl) {
		this.tipoSsl = tipoSsl;
	}
	public String getSslKsType() {
		return sslKsType;
	}
	public void setSslKsType(String sslKsType) {
		this.sslKsType = sslKsType;
	}
	public String getSslKsLocation() {
		return sslKsLocation;
	}
	public void setSslKsLocation(String sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}
	public String getSslKsPasswd() {
		return sslKsPasswd;
	}
	public void setSslKsPasswd(String sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}
	public String getSslPKeyPasswd() {
		return sslPKeyPasswd;
	}
	public void setSslPKeyPasswd(String sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}
	public String getSslTsType() {
		return sslTsType;
	}
	public void setSslTsType(String sslTsType) {
		this.sslTsType = sslTsType;
	}
	public String getSslTsLocation() {
		return sslTsLocation;
	}
	public void setSslTsLocation(String sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}
	public String getSslTsPasswd() {
		return sslTsPasswd;
	}
	public void setSslTsPasswd(String sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}
	public String getSslType() {
		return sslType;
	}
	public void setSslType(String sslType) {
		this.sslType = sslType;
	}
	public String getHttpUser() {
		return httpUser;
	}
	public void setHttpUser(String httpUser) {
		this.httpUser = httpUser;
	}
	public String getHttpPassw() {
		return httpPassw;
	}
	public void setHttpPassw(String httpPassw) {
		this.httpPassw = httpPassw;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAzioneInUrl() {
		return azioneInUrl;
	}
	public void setAzioneInUrl(boolean azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}

	@Override
	public boolean equals(Object obj) {
		Connettore connettore = null;
		if(obj instanceof Connettore) {
			connettore = (Connettore) obj;
		} else {
			return false;
		}
		
		boolean equal = 
				equals(tipoAutenticazione, connettore.getTipoAutenticazione()) &&
				equals(tipoSsl, connettore.getTipoSsl()) &&
				equals(sslKsType, connettore.getSslKsType()) &&
				equals(sslKsLocation, connettore.getSslKsLocation()) &&
				equals(sslKsPasswd, connettore.getSslKsPasswd()) &&
				equals(sslPKeyPasswd, connettore.getSslPKeyPasswd()) &&
				equals(sslTsType, connettore.getSslTsType()) &&
				equals(sslTsLocation, connettore.getSslTsLocation()) &&
				equals(sslTsPasswd, connettore.getSslTsPasswd()) &&
				equals(sslType, connettore.getSslType()) &&
				equals(httpUser, connettore.getHttpUser()) &&
				equals(httpPassw, connettore.getHttpPassw()) &&
				equals(url, connettore.getUrl()) &&
				azioneInUrl==connettore.isAzioneInUrl();

		return equal;
	}

}
