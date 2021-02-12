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

public class ConnettoreNotificaPagamenti extends Connettore implements Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String P_TIPO_TRACCIATO = "TIPO_TRACCIATO";
	public static final String P_ABILITATO = "ABILITATO";
	public static final String P_VERSIONE_CSV = "VERSIONE_CSV";
	public static final String P_TIPI_PENDENZA = "TIPI_PENDENZA";
	public static final String P_CODICE_IPA = "CODICE_IPA";
	public static final String P_TIPO_CONNETTORE = "TIPO_CONNETTORE";
	public static final String P_EMAIL_INDIRIZZO = "EMAIL_INDIRIZZO";
	public static final String P_FILE_SYSTEM_PATH = "FILE_SYSTEM_PATH";
	
	public static final String P_EMAIL_SERVER_HOST = "EMAIL_SERVER_HOST";
	public static final String P_EMAIL_SERVER_PORT = "EMAIL_SERVER_PORT";
	public static final String P_EMAIL_SERVER_USERNAME = "EMAIL_SERVER_USERNAME";
	public static final String P_EMAIL_SERVER_PASSWORD = "EMAIL_SERVER_PASSWORD";
	public static final String P_EMAIL_SERVER_FROM = "EMAIL_SERVER_FROM";
	public static final String P_EMAIL_SERVER_READTIMEOUT = "EMAIL_SERVER_READTIMEOUT";
	public static final String P_EMAIL_SERVER_CONNECTIONTIMEOUT = "EMAIL_SERVER_CONNECTIONTIMEOUT";
	
	public static final String P_CODICE_CLIENTE = "CODICE_CLIENTE";
	public static final String P_CODICE_ISTITUTO = "CODICE_ISTITUTO";
	
	public enum TipoConnettore {
		WEB_SERVICE, EMAIL, FILE_SYSTEM;
	}
	
	public enum Tipo {
		MYPIVOT, SECIM;
	}

	private String idConnettore;
	private TipoConnettore tipoConnettore;
	private boolean abilitato;
	private String tipoTracciato;
	private String versioneCsv;
	private List<String> tipiPendenza;
	private String emailIndirizzo;
	private String fileSystemPath;
	private String codiceIPA;
	private MailServer mailserver;
	private String codiceCliente;
	private String codiceIstituto;
	
	public ConnettoreNotificaPagamenti() {
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

	public MailServer getMailserver() {
		return mailserver;
	}

	public void setMailserver(MailServer mailserver) {
		this.mailserver = mailserver;
	}

	public String getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public String getCodiceIstituto() {
		return codiceIstituto;
	}

	public void setCodiceIstituto(String codiceIstituto) {
		this.codiceIstituto = codiceIstituto;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
