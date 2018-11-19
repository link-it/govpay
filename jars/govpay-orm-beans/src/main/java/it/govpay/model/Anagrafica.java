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
	private String area;
	private String pec;
	private TIPO tipo;
	public enum TIPO {F,G}
	
	public String getRagioneSociale() {
		return this.ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getCodUnivoco() {
		return this.codUnivoco;
	}
	public void setCodUnivoco(String codUnivoco) {
		this.codUnivoco = codUnivoco;
	}
	public String getIndirizzo() {
		return this.indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return this.civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCap() {
		return this.cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getLocalita() {
		return this.localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return this.provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return this.nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return this.telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return this.fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCellulare() {
		return this.cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public String getUrlSitoWeb() {
		return this.urlSitoWeb;
	}
	public void setUrlSitoWeb(String urlSitoWeb) {
		this.urlSitoWeb = urlSitoWeb;
	}
	public String getArea() {
		return this.area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPec() {
		return this.pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public TIPO getTipo() {
		return this.tipo;
	}
	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}
}
