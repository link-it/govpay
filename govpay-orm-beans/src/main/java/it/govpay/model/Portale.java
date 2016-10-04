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

import java.util.List;

public class Portale extends Versionabile {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codPortale;
	private String principal;
	private String defaultCallbackURL;
	private boolean abilitato;
	private boolean trusted;
	private List<Acl> acls;
   
	public String getDefaultCallbackURL() {
		return defaultCallbackURL;
	}
	public void setDefaultCallbackURL(String defaultCallbackURL) {
		this.defaultCallbackURL = defaultCallbackURL;
	}
    public Long getId() {
		return id;
	}
	public String getCodPortale() {
		return codPortale;
	}
	public void setCodPortale(String codPortale) {
		this.codPortale = codPortale;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public List<Acl> getAcls() {
		return acls;
	}
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}
	public boolean isTrusted() {
		return trusted;
	}
	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}
}
