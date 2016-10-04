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

import java.math.BigDecimal;

import it.govpay.bd.model.BasicModel;

public class FrApplicazione extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idFr;
	private long idApplicazione;
	private long numeroPagamenti;
	private BigDecimal importoTotalePagamenti;
	
	public FrApplicazione() {
		this.importoTotalePagamenti = BigDecimal.ZERO;
		this.numeroPagamenti = 0;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdFr() {
		return idFr;
	}
	public void setIdFr(long idFr) {
		this.idFr = idFr;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public long getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(long numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public BigDecimal getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(BigDecimal importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
}
