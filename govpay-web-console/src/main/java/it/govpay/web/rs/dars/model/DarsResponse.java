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
package it.govpay.web.rs.dars.model;

public class DarsResponse {
	
	public enum EsitoOperazione {ESEGUITA, NONESEGUITA, ERRORE} 
	
	private String codOperazione;
	private EsitoOperazione esitoOperazione;
	private String dettaglioEsito;
	private Object response;
	
	public String getCodOperazione() {
		return codOperazione;
	}
	public void setCodOperazione(String codOperazione) {
		this.codOperazione = codOperazione;
	}
	public EsitoOperazione getEsitoOperazione() {
		return esitoOperazione;
	}
	public void setEsitoOperazione(EsitoOperazione esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}
	public String getDettaglioEsito() {
		return dettaglioEsito;
	}
	public void setDettaglioEsito(String dettaglioEsito) {
		this.dettaglioEsito = dettaglioEsito;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}

}
