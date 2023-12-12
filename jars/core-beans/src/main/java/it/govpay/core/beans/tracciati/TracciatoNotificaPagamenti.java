/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans.tracciati;

import java.util.Date;

public class TracciatoNotificaPagamenti {
	
	private long numRtTotali;
	private long lineaElaborazione;
	
	private Date dataUltimoAggiornamento;
	private String stepElaborazione;
	private String descrizioneStepElaborazione;
	
	private String requestToken;
	private String uploadUrl;
	private String authorizationToken;
	private String importPath;
	
	public long getNumRtTotali() {
		return numRtTotali;
	}
	public void setNumRtTotali(long numRtTotali) {
		this.numRtTotali = numRtTotali;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public String getStepElaborazione() {
		return stepElaborazione;
	}
	public void setStepElaborazione(String stepElaborazione) {
		this.stepElaborazione = stepElaborazione;
	}
	public String getDescrizioneStepElaborazione() {
		return descrizioneStepElaborazione;
	}
	public void setDescrizioneStepElaborazione(String descrizioneStepElaborazione) {
		this.descrizioneStepElaborazione = descrizioneStepElaborazione;
	}
	public String getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	public String getUploadUrl() {
		return uploadUrl;
	}
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	public String getAuthorizationToken() {
		return authorizationToken;
	}
	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}
	public String getImportPath() {
		return importPath;
	}
	public void setImportPath(String importPath) {
		this.importPath = importPath;
	}
}
