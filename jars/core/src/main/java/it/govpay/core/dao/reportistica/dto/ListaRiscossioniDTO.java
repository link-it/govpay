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
package it.govpay.core.dao.reportistica.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.reportistica.statistiche.FiltroRiscossioni;

public class ListaRiscossioniDTO extends BasicFindRequestDTO{
	
	public enum GROUP_BY { DIV, DIR, DOMINIO, TIPO_PENDENZA, UO, APPLICAZIONE, TASSONOMIA}
	
	public ListaRiscossioniDTO(Authentication user) {
		super(user);
	}
	
	private FiltroRiscossioni filtro;
	private List<GROUP_BY> groupBy = new ArrayList<ListaRiscossioniDTO.GROUP_BY>();
	
	public FiltroRiscossioni getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroRiscossioni filtro) {
		this.filtro = filtro;
	}
	public List<GROUP_BY> getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(List<GROUP_BY> groupBy) {
		this.groupBy = groupBy;
	}
	
}
