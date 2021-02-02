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

import java.util.List;

public class ConnettoreMyPivot extends Versionabile implements Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String P_TIPO_TRACCIATO = "TIPO_TRACCIATO";
	public static final String P_ABILITATO = "ABILITATO";
	public static final String P_VERSIONE_CSV = "VERSIONE_CSV";
	public static final String P_TIPI_PENDENZA = "TIPI_PENDENZA";
	public static final String P_CODICE_IPA = "CODICE_IPA";
	public static final String P_TIPO_CONNETTORE = "TIPO_CONNETTORE";
	public static final String P_WEB_SERVICE_URL = "WEB_SERVICE_URL";
	public static final String P_WEB_SERVICE_USERNAME = "WEB_SERVICE_USERNAME";
	public static final String P_WEB_SERVICE_PASSWORD = "WEB_SERVICE_PASSWORD";
	public static final String P_EMAIL_INDIRIZZO = "EMAIL_INDIRIZZO";
	public static final String P_FILE_SYSTEM_PATH = "FILE_SYSTEM_PATH";
	
	public enum TipoConnettore {
		WEB_SERVICE, EMAIL, FILE_SYSTEM;
	}
	
	public enum Tipo {
		MYPIVOT;
	}

	private String idConnettore;
	private TipoConnettore tipoConnettore;
	private boolean abilitato;
	private String tipoTracciato;
	private String versioneCsv;
	private List<String> tipiPendenza;
	private String webServiceUrl;
	private String webServiceUsername;
	private String webServicePassword;
	private String emailIndirizzo;
	private String fileSystemPath;
	private String codiceIPA;
	
	public ConnettoreMyPivot() {
	}
		
	public String getIdConnettore() {
		return this.idConnettore;
	}
	public void setIdConnettore(String idConnettore) {
		this.idConnettore = idConnettore;
	}
	
	public TipoConnettore getTipoConnettore() {
		return tipoConnettore;
	}

	public void setTipoConnettore(TipoConnettore tipoConnettore) {
		this.tipoConnettore = tipoConnettore;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public String getTipoTracciato() {
		return tipoTracciato;
	}

	public void setTipoTracciato(String tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
	}

	public String getVersioneCsv() {
		return versioneCsv;
	}

	public void setVersioneCsv(String versioneCsv) {
		this.versioneCsv = versioneCsv;
	}

	public List<String> getTipiPendenza() {
		return tipiPendenza;
	}

	public void setTipiPendenza(List<String> tipiPendenza) {
		this.tipiPendenza = tipiPendenza;
	}

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

	public void setWebServiceUrl(String webServiceUrl) {
		this.webServiceUrl = webServiceUrl;
	}

	public String getWebServiceUsername() {
		return webServiceUsername;
	}

	public void setWebServiceUsername(String webServiceUsername) {
		this.webServiceUsername = webServiceUsername;
	}

	public String getWebServicePassword() {
		return webServicePassword;
	}

	public void setWebServicePassword(String webServicePassword) {
		this.webServicePassword = webServicePassword;
	}

	public String getEmailIndirizzo() {
		return emailIndirizzo;
	}

	public void setEmailIndirizzo(String emailIndirizzo) {
		this.emailIndirizzo = emailIndirizzo;
	}

	public String getFileSystemPath() {
		return fileSystemPath;
	}

	public void setFileSystemPath(String fileSystemPath) {
		this.fileSystemPath = fileSystemPath;
	}

	public String getCodiceIPA() {
		return codiceIPA;
	}

	public void setCodiceIPA(String codiceIPA) {
		this.codiceIPA = codiceIPA;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
