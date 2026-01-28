/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

public class IdUnitaOperativa extends it.govpay.model.IdUnitaOperativa{
	
	private static final long serialVersionUID = 1L;
	
	private String codDominio;
	private String ragioneSociale;
	
	private String codUO;
	private String ragioneSocialeUO;
	
	
	public IdUnitaOperativa() {}
	
	public IdUnitaOperativa (it.govpay.model.IdUnitaOperativa base) {
		this.setId(base.getId()); 
		this.setIdDominio(base.getIdDominio());
		this.setIdUnita(base.getIdUnita());
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodUO() {
		return codUO;
	}

	public void setCodUO(String codUO) {
		this.codUO = codUO;
	}

	public String getRagioneSocialeUO() {
		return ragioneSocialeUO;
	}

	public void setRagioneSocialeUO(String ragioneSocialeUO) {
		this.ragioneSocialeUO = ragioneSocialeUO;
	}
	
}
