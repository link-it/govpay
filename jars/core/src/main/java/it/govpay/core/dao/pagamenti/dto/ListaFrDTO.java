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

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Fr;
import it.govpay.orm.FR;

public class ListaFrDTO extends BasicFindRequestDTO{
	
	public ListaFrDTO(Authentication user) {
		super(user);
		this.addSortField("data", FR.model().DATA_ACQUISIZIONE);
		this.addSortField("idFlusso", FR.model().COD_FLUSSO);
		this.addDefaultSort(FR.model().DATA_ACQUISIZIONE,SortOrder.DESC);
	}
	private String idDominio;
	private Date dataDa;
	private Date dataA;
	private Boolean incassato = null;
	private String idFlusso;
	private Fr.StatoFr stato;
	private Boolean obsoleto = null;
	private String iuv;
	private boolean ricercaIdFlussoCaseInsensitive = false;

	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public Date getDataDa() {
		return this.dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return this.dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Boolean getIncassato() {
		return incassato;
	}
	public void setIncassato(Boolean incassato) {
		this.incassato = incassato;
	}
	public String getIdFlusso() {
		return idFlusso;
	}
	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}
	public Fr.StatoFr getStato() {
		return stato;
	}
	public void setStato(Fr.StatoFr stato) {
		this.stato = stato;
	}
	public Boolean getObsoleto() {
		return obsoleto;
	}
	public void setObsoleto(Boolean obsoleto) {
		this.obsoleto = obsoleto;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}
	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}
}
