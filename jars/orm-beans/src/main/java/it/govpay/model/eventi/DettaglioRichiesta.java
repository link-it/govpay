/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.model.eventi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DettaglioRichiesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private String principal;
	private String utente;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "it_IT", timezone = "Europe/Rome")
	private Date dataOraRichiesta;
	private String url;
//	private Map<String, String> headers;
	private List<Header> headers;
	private String payload; 
	private String method;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public Date getDataOraRichiesta() {
		return dataOraRichiesta;
	}
	public void setDataOraRichiesta(Date dataOraRichiesta) {
		this.dataOraRichiesta = dataOraRichiesta;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Header> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}
	public void setHeadersFromMap(Map<String, String> headers) {
		if(headers != null) {
			this.headers = new ArrayList<>();
			
			for (Entry<String, String> entry : headers.entrySet()) {
				this.headers.add(new Header(entry.getKey(), entry.getValue()));	
			}
		}
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
