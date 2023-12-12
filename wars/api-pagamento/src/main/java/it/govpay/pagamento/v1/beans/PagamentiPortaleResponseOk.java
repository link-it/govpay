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
package it.govpay.pagamento.v1.beans;

import it.govpay.core.beans.JSONSerializable;

public class PagamentiPortaleResponseOk extends JSONSerializable {

	private String id;
	private String location;
	private String redirect;
	private String idSession;

	public String getIdSession() {
		return this.idSession;
	}
	public void setIdSession(String idSession) {
		this.idSession = idSession;
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
	public String getRedirect() {
		return this.redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortaleResponse";
	}
}
