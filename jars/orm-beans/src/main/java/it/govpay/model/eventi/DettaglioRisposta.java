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
package it.govpay.model.eventi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DettaglioRisposta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "it_IT", timezone = "Europe/Rome")
	private Date dataOraRisposta;
	private Integer status;
//	private Map<String, String> headers;
	private List<Header> headers;
//	private List<Entry<String, String>> headers;
	
	private String payload;
	
	public Date getDataOraRisposta() {
		return dataOraRisposta;
	}
	public void setDataOraRisposta(Date dataOraRisposta) {
		this.dataOraRisposta = dataOraRisposta;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}
