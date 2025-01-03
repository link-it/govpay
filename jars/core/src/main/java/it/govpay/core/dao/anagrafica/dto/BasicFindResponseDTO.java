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

public class BasicFindResponseDTO<T> {
	
	private Long totalResults;
	private List<T> results;
	
	public BasicFindResponseDTO(Long totalResults, List<T> results) {
		this.totalResults = totalResults;
		this.results = results;
	}

	public Long getTotalResults() {
		return this.totalResults;
	}

	public void setTotalResults(Long totalResults) {
		this.totalResults = totalResults;
	}

	public List<T> getResults() {
		return this.results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}



}
