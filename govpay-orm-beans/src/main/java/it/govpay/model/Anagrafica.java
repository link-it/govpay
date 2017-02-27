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

public class Anagrafica extends BasicModel {
	private static final long serialVersionUID = 1L;
	private String ragioneSociale;
	private String codUnivoco;
	private String indirizzo;
	private String civico;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private String email;
	private String telefono;
	private String cellulare;
	private String fax;
	private String urlSitoWeb;
	
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getCodUnivoco() {
		return codUnivoco;
	}
	public void setCodUnivoco(String codUnivoco) {
		this.codUnivoco = codUnivoco;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getLocalita() {
		return localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public String getUrlSitoWeb() {
		return urlSitoWeb;
	}
	public void setUrlSitoWeb(String urlSitoWeb) {
		this.urlSitoWeb = urlSitoWeb;
	}
	@Override
	public boolean equals(Object obj) {
		Anagrafica anagrafica = null;
		if(obj instanceof Anagrafica) {
			anagrafica = (Anagrafica) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(cap, anagrafica.getCap()) &&
				equals(cellulare, anagrafica.getCellulare()) &&
				equals(civico, anagrafica.getCivico()) &&
				equals(codUnivoco, anagrafica.getCodUnivoco()) &&
				equals(email, anagrafica.getEmail()) &&
				equals(fax, anagrafica.getFax()) &&
				equals(indirizzo, anagrafica.getIndirizzo()) &&
				equals(localita, anagrafica.getLocalita()) &&
				equals(nazione, anagrafica.getNazione()) &&
				equals(provincia, anagrafica.getProvincia()) &&
				equals(ragioneSociale, anagrafica.getRagioneSociale()) &&
				equals(telefono, anagrafica.getTelefono()) &&
				equals(urlSitoWeb, anagrafica.getUrlSitoWeb());
		
		return equal;
	}
}
