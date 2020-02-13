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
package it.govpay.core.dao.anagrafica.dto;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

public class FindUnitaOperativeDTO extends BasicFindRequestDTO {
	
	private String codDominio;
	private String codIdentificativo;
	private String ragioneSociale;
	private Boolean abilitato;
	private Boolean associati;

	public FindUnitaOperativeDTO(Authentication user, String codDominio) {
		super(user);
		this.codDominio = codDominio;
		this.addSortField("ragioneSociale", it.govpay.orm.Uo.model().UO_DENOMINAZIONE);
		this.addDefaultSort(it.govpay.orm.Uo.model().COD_UO, SortOrder.ASC);
		this.addDefaultSort(it.govpay.orm.Uo.model().UO_DENOMINAZIONE, SortOrder.ASC);
	}
	
	public String getCodDominio(){
		return this.codDominio;
	}

	public String getCodIdentificativo() {
		return this.codIdentificativo;
	}

	public void setCodIdentificativo(String codIdentificativo) {
		this.codIdentificativo = codIdentificativo;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public Boolean getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	public Boolean getAssociati() {
		return associati;
	}

	public void setAssociati(Boolean associati) {
		this.associati = associati;
	}
	
}
