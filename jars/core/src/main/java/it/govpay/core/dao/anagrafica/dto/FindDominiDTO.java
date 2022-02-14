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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

public class FindDominiDTO extends BasicFindRequestDTO {
	
	private String codStazione = null;
	private String codDominio = null;
	private String ragioneSociale = null;
	private Boolean abilitato = null;
	private Boolean formBackoffice;
	private Boolean formPortalePagamento;
	private Boolean intermediato = null;
	
	public FindDominiDTO(Authentication user) throws ServiceException {
		super(user);
		this.addSortField("codDominio", it.govpay.orm.Dominio.model().COD_DOMINIO);
		this.addSortField("ragioneSociale", it.govpay.orm.Dominio.model().RAGIONE_SOCIALE);
		this.addDefaultSort(it.govpay.orm.Dominio.model().RAGIONE_SOCIALE, SortOrder.ASC);
		this.addDefaultSort(it.govpay.orm.Dominio.model().COD_DOMINIO, SortOrder.ASC);
		this.ricercaAnagrafica = true;
	}

	public String getCodStazione() {
		return this.codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
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

	public Boolean getFormBackoffice() {
		return formBackoffice;
	}

	public void setFormBackoffice(Boolean formBackoffice) {
		this.formBackoffice = formBackoffice;
	}

	public Boolean getFormPortalePagamento() {
		return formPortalePagamento;
	}

	public void setFormPortalePagamento(Boolean formPortalePagamento) {
		this.formPortalePagamento = formPortalePagamento;
	}

	public Boolean getIntermediato() {
		return intermediato;
	}

	public void setIntermediato(Boolean intermediato) {
		this.intermediato = intermediato;
	}
}
