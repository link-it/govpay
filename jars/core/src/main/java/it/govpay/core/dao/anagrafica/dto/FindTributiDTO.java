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

public class FindTributiDTO extends BasicFindRequestDTO {
	
	private String codDominio;
	private String codTributo;
	private String descrizione;
	private Boolean abilitato;
	
	public FindTributiDTO(Authentication user, String codDominio) {
		super(user);
		this.codDominio = codDominio;
		this.addSortField("idEntrata", it.govpay.orm.Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO);
		this.addSortField("descrizione", it.govpay.orm.Tributo.model().TIPO_TRIBUTO.DESCRIZIONE);
		this.addDefaultSort(it.govpay.orm.Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO, SortOrder.ASC);
		this.addDefaultSort(it.govpay.orm.Tributo.model().TIPO_TRIBUTO.DESCRIZIONE, SortOrder.ASC);
	}
	
	public String getCodDominio(){
		return this.codDominio;
	}

	public String getCodTributo() {
		return this.codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getDescrizione() {
		return this.descrizione;
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

}
