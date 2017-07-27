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
package it.govpay.core.business.model;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Dominio;

public class ListaDominiDTOResponse {
	
	private List<Dominio> domini;
	private long totalCount;

	public ListaDominiDTOResponse() {
		setDomini(new ArrayList<Dominio>());
	}

	public List<Dominio> getDomini() {
		return domini;
	}

	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

}
