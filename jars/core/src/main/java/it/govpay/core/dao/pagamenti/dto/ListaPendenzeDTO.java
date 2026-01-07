/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
import it.govpay.model.StatoPendenza;
import it.govpay.orm.VistaVersamento;

public class ListaPendenzeDTO extends BasicFindRequestDTO{
	
	
	public ListaPendenzeDTO(Authentication user) {
		super(user);
		this.addSortField("dataCaricamento", VistaVersamento.model().DATA_CREAZIONE);
		this.addSortField("dataValidita", VistaVersamento.model().DATA_VALIDITA);
		this.addSortField("dataScadenza", VistaVersamento.model().DATA_SCADENZA);
		this.addSortField("stato", VistaVersamento.model().STATO_VERSAMENTO);
		this.addDefaultSort(VistaVersamento.model().DATA_CREAZIONE,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private StatoPendenza stato;
	private String idDominio;
	private String idPagamento;
	private String idDebitore;
	private String idA2A;
	private String idPendenza;
	private String iuv;
	private boolean abilitaFiltroCittadino;
	private String idTipoVersamento;
	private String divisione;
	private String direzione;
	private String cfCittadino;
	private Boolean mostraSpontaneiNonPagati;
	
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
	public StatoPendenza getStato() {
		return this.stato;
	}
	public void setStato(StatoPendenza stato) {
		this.stato = stato;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdPagamento() {
		return this.idPagamento;
	}
	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}
	public String getIdDebitore() {
		return this.idDebitore;
	}
	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIdTipoVersamento() {
		return idTipoVersamento;
	}
	public void setIdTipoVersamento(String idTipoVersamento) {
		this.idTipoVersamento = idTipoVersamento;
	}
	public String getDivisione() {
		return divisione;
	}

	public void setDivisione(String divisione) {
		this.divisione = divisione;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}
	public boolean isAbilitaFiltroCittadino() {
		return abilitaFiltroCittadino;
	}
	public void setAbilitaFiltroCittadino(boolean abilitaFiltroCittadino) {
		this.abilitaFiltroCittadino = abilitaFiltroCittadino;
	}
	public String getCfCittadino() {
		return cfCittadino;
	}
	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}
	public Boolean getMostraSpontaneiNonPagati() {
		return mostraSpontaneiNonPagati;
	}
	public void setMostraSpontaneiNonPagati(Boolean mostraSpontaneiNonPagati) {
		this.mostraSpontaneiNonPagati = mostraSpontaneiNonPagati;
	}
}
