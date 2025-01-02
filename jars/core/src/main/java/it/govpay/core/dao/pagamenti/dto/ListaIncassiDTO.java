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
package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Incasso.StatoIncasso;
import it.govpay.orm.Incasso;

public class ListaIncassiDTO extends BasicFindRequestDTO {
	
	public ListaIncassiDTO(Authentication user) {
		super(user);
		this.addSortField("data", Incasso.model().DATA_ORA_INCASSO);
		this.addDefaultSort(Incasso.model().DATA_ORA_INCASSO,SortOrder.DESC);
		this.includiPagamenti = false;
	}
	
	private String idA2A;
	private String idDominio;
	private Date dataDa;
	private Date dataA;
	private String sct;
	private boolean includiPagamenti;
	private String codFlusso;
	private String iuv;
	private StatoIncasso stato;

	 
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public String getIdA2A() {
		return idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getSct() {
		return sct;
	}
	public void setSct(String sct) {
		this.sct = sct;
	}
	public boolean isIncludiPagamenti() {
		return includiPagamenti;
	}
	public void setIncludiPagamenti(boolean includiPagamenti) {
		this.includiPagamenti = includiPagamenti;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public StatoIncasso getStato() {
		return stato;
	}
	public void setStato(StatoIncasso stato) {
		this.stato = stato;
	}
}
