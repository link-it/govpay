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
package it.govpay.orm.gde;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gde_eventi")
public class GdeEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Timestamp dtEvento;
	private String idDominio;
	private String idUnivocoVersamento;
	private String codiceContestoPagamento;
	private String idPrestatoreserviziPagamento;
	private String tipoVersamento;
	private String componente;
	private String categoriaEvento;
	private String tipoEvento;
	private String sottoTipoEvento;
	private String idFruitore;
	private String idErogatore;
	private String idStazioneIntermediarioPa;
	private String canalePagamento;
	private String paramSpecificiInterfaccia;
	private String esito;
	private String idEgov;
	private Long id;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "dt_evento")
	public Timestamp getDtEvento() {
		return dtEvento;
	}

	public void setDtEvento(Timestamp dtEvento) {
		this.dtEvento = dtEvento;
	}

	@Column(name = "id_dominio")
	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	@Column(name = "id_univoco_versamento")
	public String getIdUnivocoVersamento() {
		return idUnivocoVersamento;
	}

	public void setIdUnivocoVersamento(String idUnivocoVersamento) {
		this.idUnivocoVersamento = idUnivocoVersamento;
	}

	@Column(name = "codice_contesto_pagamento")
	public String getCodiceContestoPagamento() {
		return codiceContestoPagamento;
	}

	public void setCodiceContestoPagamento(String codiceContestoPagamento) {
		this.codiceContestoPagamento = codiceContestoPagamento;
	}

	@Column(name = "id_prestatoreservizi_pagamento")
	public String getIdPrestatoreserviziPagamento() {
		return idPrestatoreserviziPagamento;
	}

	public void setIdPrestatoreserviziPagamento(
			String idPrestatoreserviziPagamento) {
		this.idPrestatoreserviziPagamento = idPrestatoreserviziPagamento;
	}

	@Column(name = "tipo_versamento")
	public String getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	@Column(name = "componente")
	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	@Column(name = "categoria_evento")
	public String getCategoriaEvento() {
		return categoriaEvento;
	}

	public void setCategoriaEvento(String categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	@Column(name = "tipo_evento")
	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	@Column(name = "sotto_tipo_evento")
	public String getSottoTipoEvento() {
		return sottoTipoEvento;
	}

	public void setSottoTipoEvento(String sottoTipoEvento) {
		this.sottoTipoEvento = sottoTipoEvento;
	}

	@Column(name = "id_fruitore")
	public String getIdFruitore() {
		return idFruitore;
	}

	public void setIdFruitore(String idFruitore) {
		this.idFruitore = idFruitore;
	}

	@Column(name = "id_erogatore")
	public String getIdErogatore() {
		return idErogatore;
	}

	public void setIdErogatore(String idErogatore) {
		this.idErogatore = idErogatore;
	}

	@Column(name = "id_stazione_intermediario_pa")
	public String getIdStazioneIntermediarioPa() {
		return idStazioneIntermediarioPa;
	}

	public void setIdStazioneIntermediarioPa(String idStazioneIntermediarioPa) {
		this.idStazioneIntermediarioPa = idStazioneIntermediarioPa;
	}

	@Column(name = "canale_pagamento")
	public String getCanalePagamento() {
		return canalePagamento;
	}

	public void setCanalePagamento(String canalePagamento) {
		this.canalePagamento = canalePagamento;
	}

	@Column(name = "param_specifici_interfaccia")
	public String getParamSpecificiInterfaccia() {
		return paramSpecificiInterfaccia;
	}

	public void setParamSpecificiInterfaccia(String paramSpecificiInterfaccia) {
		this.paramSpecificiInterfaccia = paramSpecificiInterfaccia;
	}

	@Column(name = "esito")
	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	@Column(name = "id_egov")
	public String getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(String idEgov) {
		this.idEgov = idEgov;
	}


}
