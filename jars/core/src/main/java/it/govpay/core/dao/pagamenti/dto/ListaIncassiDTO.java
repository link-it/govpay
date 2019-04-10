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
package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.orm.Incasso;

public class ListaIncassiDTO extends BasicFindRequestDTO {
	
	public ListaIncassiDTO(Authentication user) {
		super(user);
		this.addDefaultSort(Incasso.model().DATA_ORA_INCASSO,SortOrder.DESC);
	}
	
	private String idA2A;
	private Date inizio;
	private Date fine;

	public Date getInizio() {
		return this.inizio;
	}
	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}
	public Date getFine() {
		return this.fine;
	}
	public void setFine(Date fine) {
		this.fine = fine;
	}
	public String getIdA2A() {
		return idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
}
