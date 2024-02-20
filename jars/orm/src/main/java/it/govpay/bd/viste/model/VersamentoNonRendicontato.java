/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.bd.viste.model;

import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.BasicModel;

public class VersamentoNonRendicontato extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private SingoloVersamento singoloVersamento;
	private Versamento versamento;
	private Pagamento pagamento;
	private Rpt rpt;
	private Incasso incasso;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SingoloVersamento getSingoloVersamento() {
		return singoloVersamento;
	}
	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}
	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public Pagamento getPagamento() {
		return pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
	public Rpt getRpt() {
		return rpt;
	}
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}
	public Incasso getIncasso() {
		return incasso;
	}
	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
	}
	
}
