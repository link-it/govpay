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
import it.govpay.model.reportistica.statistiche.FiltroRendicontazioni;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public enum GROUP_BY { DIV, DIR, FLUSSO_RENDICONTAZIONE}
	
	public ListaRendicontazioniDTO(Authentication user) {
		super(user);
	}
	
	private FiltroRendicontazioni filtro;
	private List<GROUP_BY> groupBy = new ArrayList<>();
	
	public FiltroRendicontazioni getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroRendicontazioni filtro) {
		this.filtro = filtro;
	}
	public List<GROUP_BY> getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(List<GROUP_BY> groupBy) {
		this.groupBy = groupBy;
	}
	
}
