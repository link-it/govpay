/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.model;

public class Psp extends BasicModel {	
	
	private static final long serialVersionUID = 1L;
	private Long id; 	
	private String codPsp;
	private String codFlusso;
	private String ragioneSociale;
	private String urlInfo;
	private boolean bolloGestito;
	private boolean stornoGestito;
	private boolean abilitato;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(String urlInfo) {
		this.urlInfo = urlInfo;
	}

	public boolean isBolloGestito() {
		return bolloGestito;
	}

	public void setBolloGestito(boolean bolloGestito) {
		this.bolloGestito = bolloGestito;
	}

	public boolean isStornoGestito() {
		return stornoGestito;
	}

	public void setStornoGestito(boolean stornoGestito) {
		this.stornoGestito = stornoGestito;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

}
