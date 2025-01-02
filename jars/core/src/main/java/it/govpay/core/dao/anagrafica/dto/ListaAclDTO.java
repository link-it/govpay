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
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.model.Acl.Diritti;
import it.govpay.orm.ACL;

public class ListaAclDTO extends BasicFindRequestDTO{

	private String principal;
	private Boolean forcePrincipal;
	private String ruolo;
	private Boolean forceRuolo;
	private String servizio;
	private Boolean forceServizio;
	private List<Diritti> diritti;
	
	public Boolean getForcePrincipal() {
		return this.forcePrincipal;
	}


	public void setForcePrincipal(Boolean forcePrincipal) {
		this.forcePrincipal = forcePrincipal;
	}


	public Boolean getForceRuolo() {
		return this.forceRuolo;
	}


	public void setForceRuolo(Boolean forceRuolo) {
		this.forceRuolo = forceRuolo;
	}


	public String getPrincipal() {
		return this.principal;
	}


	public void setPrincipal(String principal) {
		this.principal = principal;
	}


	public String getRuolo() {
		return this.ruolo;
	}


	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}


	public String getServizio() {
		return this.servizio;
	}


	public void setServizio(String servizio) {
		this.servizio = servizio;
	}


	public List<Diritti> getDiritti() {
		return this.diritti;
	}


	public void setDiritti(List<Diritti> diritti) {
		this.diritti = diritti;
	}


	public ListaAclDTO(Authentication user) {
		super(user);
		this.addSortField("principal", ACL.model().ID_UTENZA.PRINCIPAL_ORIGINALE);
		this.addSortField("ruolo", ACL.model().RUOLO);
		this.addSortField("servizio", ACL.model().SERVIZIO);
	}


	public Boolean getForceServizio() {
		return this.forceServizio;
	}


	public void setForceServizio(Boolean forceServizio) {
		this.forceServizio = forceServizio;
	}
}
