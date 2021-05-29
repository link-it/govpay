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

public class FindTipiPendenzaDTO extends BasicFindRequestDTO {
	
	
	private String codDominio;
	private String codTipoVersamento;
	private String descrizione;
	private Boolean abilitato;
	private Boolean formBackoffice;
	private Boolean formPortalePagamento;
	private Boolean trasformazione;
	private String nonAssociati;
		
	public FindTipiPendenzaDTO(Authentication user) throws ServiceException {
		super(user);
		this.addSortField("idTipoPendenza", it.govpay.orm.TipoVersamento.model().COD_TIPO_VERSAMENTO);
		this.addSortField("descrizione", it.govpay.orm.TipoVersamento.model().DESCRIZIONE);
		this.addDefaultSort(it.govpay.orm.TipoVersamento.model().DESCRIZIONE, SortOrder.ASC);
		this.addDefaultSort(it.govpay.orm.TipoVersamento.model().COD_TIPO_VERSAMENTO, SortOrder.ASC);
		this.ricercaAnagrafica = true;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public Boolean getTrasformazione() {
		return trasformazione;
	}

	public void setTrasformazione(Boolean trasformazione) {
		this.trasformazione = trasformazione;
	}

	public String getNonAssociati() {
		return nonAssociati;
	}

	public void setNonAssociati(String nonAssociati) {
		this.nonAssociati = nonAssociati;
	}
	
}
