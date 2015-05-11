/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.orm.configurazione;

import it.govpay.orm.BaseEntity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cfg_commissione_pagamento")
public class CfgCommissionePagamento extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal importoA;
	private BigDecimal importoDa;
	private String divisa;
	private BigDecimal valore;
	private String stRiga;
	private String descrizione;
	private CfgGatewayPagamento cfgGatewayPagamento;
	private CfgTipoCommissione cfgTipoCommissione;

	private BigDecimal importoCalcolato;
	/*** Auto Generated Identity Property ***/
	private Long id;

	public CfgCommissionePagamento() {
	}

	@Column(name = "IMPORTO_A", nullable = false)
	public BigDecimal getImportoA() {
		return this.importoA;
	}

	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}

	@Column(name = "IMPORTO_DA", nullable = false)
	public BigDecimal getImportoDa() {
		return this.importoDa;
	}

	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	@Column(name = "ST_RIGA", nullable = false)
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	// bi-directional many-to-one association to CfgGatewayPagamento
	@ManyToOne
	@JoinColumn(name = "ID_CFG_GATEWAY_PAGAMENTO")
	public CfgGatewayPagamento getCfgGatewayPagamento() {
		return this.cfgGatewayPagamento;
	}

	public void setCfgGatewayPagamento(CfgGatewayPagamento cfgGatewayPagamento) {
		this.cfgGatewayPagamento = cfgGatewayPagamento;
	}

	// bi-directional many-to-one association to CfgTipoCommissione
	@ManyToOne
	@JoinColumn(name = "ID_CFG_TIPO_COMMISSIONE", nullable = false)
	public CfgTipoCommissione getCfgTipoCommissione() {
		return this.cfgTipoCommissione;
	}

	public void setCfgTipoCommissione(CfgTipoCommissione cfgTipoCommissione) {
		this.cfgTipoCommissione = cfgTipoCommissione;
	}

	@Column(name = "DIVISA")
	public String getDivisa() {
		return this.divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	@Column(nullable = false)
	public BigDecimal getValore() {
		return this.valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	@Transient
	public BigDecimal getImportoCalcolato() {
		return importoCalcolato;
	}

	public void setImportoCalcolato(BigDecimal importoCalcolato) {
		this.importoCalcolato = importoCalcolato;
	}

	@Column(name = "DESCRIZIONE")
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CfgCommissionePagamento [getImportoA()=");
		builder.append(getImportoA());
		builder.append(", getImportoDa()=");
		builder.append(getImportoDa());
		builder.append(", getStRiga()=");
		builder.append(getStRiga());
		builder.append(", getCfgTipoCommissione()=");
		builder.append(getCfgTipoCommissione());
		builder.append(", getDivisa()=");
		builder.append(getDivisa());
		builder.append(", getValore()=");
		builder.append(getValore());
		builder.append(", getImportoCalcolato()=");
		builder.append(getImportoCalcolato());
		builder.append(", getDescrizione()=");
		builder.append(getDescrizione());
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getVersion()=");
		builder.append(getVersion());
		builder.append(", getOpInserimento()=");
		builder.append(getOpInserimento());
		builder.append(", getOpAggiornamento()=");
		builder.append(getOpAggiornamento());
		builder.append(", getTsInserimento()=");
		builder.append(getTsInserimento());
		builder.append(", getTsAggiornamento()=");
		builder.append(getTsAggiornamento());
		builder.append("]");
		return builder.toString();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}