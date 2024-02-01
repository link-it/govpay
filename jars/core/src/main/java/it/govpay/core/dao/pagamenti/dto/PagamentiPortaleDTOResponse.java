/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti.dto;

public class PagamentiPortaleDTOResponse {

	private String id;
	private String idSessione;
	private String location;
	private String redirectUrl;
	private String idSessionePsp;
	private String idCarrelloRpt;
	private boolean redirect = false;
		
	public String getIdSessionePsp() {
		return idSessionePsp;
	}
	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}
	public String getIdCarrelloRpt() {
		return idCarrelloRpt;
	}
	public void setIdCarrelloRpt(String idCarrelloRpt) {
		this.idCarrelloRpt = idCarrelloRpt;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRedirectUrl() {
		return this.redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getIdSessione() {
		return this.idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
}
