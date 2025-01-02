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
package it.govpay.backoffice.v1.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"acl"
})

public class ListaAcl extends JSONSerializable {

	@JsonProperty("acl")
	private List<AclPost> acl;
	public ListaAcl() {
		super();
	}

	/* (non-Javadoc)
	 * @see it.govpay.core.rs.v1.beans.JSONSerializable#getJsonIdFilter()
	 */
	@Override
	public String getJsonIdFilter() {
		return "acl";
	}

	@JsonProperty("acl")
	public List<AclPost> getAcl() {
		return this.acl;
	}

	public void setAcl(List<AclPost> acl) {
		this.acl = acl;
	}
}
