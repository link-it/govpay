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
package it.govpay.core.dao.reportistica.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.orm.VistaRiscossioni;

public class ListaEntratePrevisteDTO extends BasicFindRequestDTO{
	
	public enum FormatoRichiesto {JSON,PDF}
	
	
	public ListaEntratePrevisteDTO(Authentication user) {
		super(user);
		this.addSortField("data", VistaRiscossioni.model().DATA_PAGAMENTO);
		this.addDefaultSort(VistaRiscossioni.model().DATA_PAGAMENTO, SortOrder.ASC);
	}
	private Date dataA;
	private Date dataDa;
	private String idDominio;
	private String idA2A;
	private FormatoRichiesto formato;
	
	public Date getDataA() {
		return this.dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return this.dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public FormatoRichiesto getFormato() {
		return formato;
	}
	public void setFormato(FormatoRichiesto formato) {
		this.formato = formato;
	}
	
}
