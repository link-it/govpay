/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.PagamentoPortale;

public class LeggiPagamentoPortaleDTOResponse {

	private PagamentoPortale pagamento = null;
	private List<LeggiRptDTOResponse> listaRpp = null;
	private List<LeggiPendenzaDTOResponse> listaPendenze = null;
	private List<Evento> eventi= null;

	public PagamentoPortale getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(PagamentoPortale pagamento) {
		this.pagamento = pagamento;
	}

	public List<LeggiRptDTOResponse> getListaRpp() {
		return this.listaRpp;
	}

	public void setListaRpp(List<LeggiRptDTOResponse> listaRpp) {
		this.listaRpp = listaRpp;
	}

	public List<LeggiPendenzaDTOResponse> getListaPendenze() {
		return this.listaPendenze;
	}

	public void setListaPendenze(List<LeggiPendenzaDTOResponse> listaPendenze) {
		this.listaPendenze = listaPendenze;
	}

	public List<Evento> getEventi() {
		return eventi;
	}

	public void setEventi(List<Evento> eventi) {
		this.eventi = eventi;
	}
	
}
