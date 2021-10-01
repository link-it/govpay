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

public class FindOperatoriDTO extends BasicFindRequestDTO {
	
	private Boolean abilitato = null;
	
	public FindOperatoriDTO(Authentication user) throws ServiceException {
		super(user);
		this.addSortField("principal", it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL);
		this.addSortField("ragioneSociale", it.govpay.orm.Operatore.model().NOME);
		this.addDefaultSort(it.govpay.orm.Operatore.model().NOME, SortOrder.ASC);
		this.ricercaAnagrafica = true;
	}

	public Boolean getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}
	
}
