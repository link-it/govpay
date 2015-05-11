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
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "cfg_gateway_pagamento")
@NamedQueries({
		@NamedQuery(name = "getGfgGatewayByListaModatlita", query = "select cgp from CfgGatewayPagamento cgp where cgp.cfgModalitaPagamento.id in :listaIdModalita and cgp.dataInizioValidita <= :dtInizioValidita and cgp.dataFineValidita >= :dtFineValidita and cgp.stRiga = :stRiga and cgp.stato = :stato"),
		@NamedQuery(name = "getGfgGatewayById", query = "select cgp from CfgGatewayPagamento cgp where cgp.id = :id and cgp.dataInizioValidita <= :dtInizioValidita and cgp.dataFineValidita >= :dtFineValidita and cgp.stRiga = :stRiga and cgp.stato = :stato")
})
public class CfgGatewayPagamento extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String applicationId;
	private String applicationIp;
	private String bundleKey;
	private Timestamp dataFineValidita;
	private Timestamp dataInizioValidita;
	private Timestamp dataPubblicazione;
	private String descGateway;
	private String descrizione;
	private BigDecimal maxImporto;
	private String molteplicita;
	private String stRiga;
	private String stato;
	private String systemId;
	private String subsystemId;
	private String systemName;
	private String disponibilitaServizio;
	private Integer timeout;
	private Integer timeoutAup;
	private Integer timeoutNp;
	private Set<CfgCommissionePagamento> cfgCommissionePagamenti;
	private CfgCanalePagamento cfgCanalePagamento;
	private CfgDocumentoPagamento cfgDocumentoPagamento;
	private CfgFornitoreGateway cfgFornitoreGateway;
	private CfgModalitaPagamento cfgModalitaPagamento;
	private CfgStrumentoPagamento cfgStrumentoPagamento;

	private String flPagabileIris = "Y"; // DEFAULT
	private String urlInfoPsp;
	private String urlInfoCanale;
	/*** Auto Generated Identity Property ***/
	private Long id;
	private String priorita;
	
	private String  flagModRiversamento;
	private Long  idContotecnico;   
	private String flagRendVersamento; 
	private String modelloVersamento; 
	private String flStornoGestito; 
	
	
	
	public CfgGatewayPagamento() {
	}

	@Column(name = "APPLICATION_ID", nullable = false)
	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "APPLICATION_IP")
	public String getApplicationIp() {
		return this.applicationIp;
	}

	public void setApplicationIp(String applicationIp) {
		this.applicationIp = applicationIp;
	}

	@Column(name = "BUNDLE_KEY", nullable = false)
	public String getBundleKey() {
		return this.bundleKey;
	}

	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}

	@Column(name = "DATA_FINE_VALIDITA", nullable = false)
	public Timestamp getDataFineValidita() {
		return this.dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	@Column(name = "DATA_INIZIO_VALIDITA", nullable = false)
	public Timestamp getDataInizioValidita() {
		return this.dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	@Column(name = "DESC_GATEWAY", nullable = false)
	public String getDescGateway() {
		return this.descGateway;
	}

	public void setDescGateway(String descGateway) {
		this.descGateway = descGateway;
	}

	@Column(nullable = false, length = 510)
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Column(name = "MAX_IMPORTO")
	public BigDecimal getMaxImporto() {
		return this.maxImporto;
	}

	public void setMaxImporto(BigDecimal maxImporto) {
		this.maxImporto = maxImporto;
	}

	@Column(length = 2)
	public String getMolteplicita() {
		return this.molteplicita;
	}

	public void setMolteplicita(String molteplicita) {
		this.molteplicita = molteplicita;
	}

	@Column(name = "ST_RIGA", nullable = false)
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(nullable = false)
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "SYSTEM_ID", nullable = false)
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "SUBSYSTEM_ID")
	public String getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(String subsystemId) {
		this.subsystemId = subsystemId;
	}

	public Integer getTimeout() {
		return this.timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	@Column(name = "TIMEOUT_AUP")
	public Integer getTimeoutAup() {
		return this.timeoutAup;
	}

	public void setTimeoutAup(Integer timeoutAup) {
		this.timeoutAup = timeoutAup;
	}

	@Column(name = "TIMEOUT_NP")
	public Integer getTimeoutNp() {
		return this.timeoutNp;
	}

	public void setTimeoutNp(Integer timeoutNp) {
		this.timeoutNp = timeoutNp;
	}

	// bi-directional many-to-one association to CfgCommissionePagamento
	@OneToMany(mappedBy = "cfgGatewayPagamento", cascade= {CascadeType.ALL})
	@OrderBy("id")
	public Set<CfgCommissionePagamento> getCfgCommissionePagamenti() {
		return this.cfgCommissionePagamenti;
	}

	public void setCfgCommissionePagamenti(Set<CfgCommissionePagamento> cfgCommissionePagamenti) {
		this.cfgCommissionePagamenti = cfgCommissionePagamenti;
	}

	// bi-directional many-to-one association to CfgCanalePagamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_CANALE_PAGAMENTO", nullable = false)
	public CfgCanalePagamento getCfgCanalePagamento() {
		return this.cfgCanalePagamento;
	}

	public void setCfgCanalePagamento(CfgCanalePagamento cfgCanalePagamento) {
		this.cfgCanalePagamento = cfgCanalePagamento;
	}

	// bi-directional many-to-one association to CfgDocumentoPagamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_DOCUMENTO_PAGAMENTO")
	public CfgDocumentoPagamento getCfgDocumentoPagamento() {
		return this.cfgDocumentoPagamento;
	}

	public void setCfgDocumentoPagamento(CfgDocumentoPagamento cfgDocumentoPagamento) {
		this.cfgDocumentoPagamento = cfgDocumentoPagamento;
	}

	// bi-directional many-to-one association to CfgFornitoreGateway
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_FORNITORE_GATEWAY", nullable = false)
	public CfgFornitoreGateway getCfgFornitoreGateway() {
		return this.cfgFornitoreGateway;
	}

	public void setCfgFornitoreGateway(CfgFornitoreGateway cfgFornitoreGateway) {
		this.cfgFornitoreGateway = cfgFornitoreGateway;
	}

	// bi-directional many-to-one association to CfgModalitaPagamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_MODALITA_PAGAMENTO")
	public CfgModalitaPagamento getCfgModalitaPagamento() {
		return this.cfgModalitaPagamento;
	}

	public void setCfgModalitaPagamento(CfgModalitaPagamento cfgModalitaPagamento) {
		this.cfgModalitaPagamento = cfgModalitaPagamento;
	}

	// bi-directional many-to-one association to CfgStrumentoPagamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_STRUMENTO_PAGAMENTO")
	public CfgStrumentoPagamento getCfgStrumentoPagamento() {
		return this.cfgStrumentoPagamento;
	}

	public void setCfgStrumentoPagamento(CfgStrumentoPagamento cfgStrumentoPagamento) {
		this.cfgStrumentoPagamento = cfgStrumentoPagamento;
	}

	@Column(name = "SYSTEM_NAME", nullable = false)
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Column(name = "DISPONIBILITA_SERVIZIO")
	public String getDisponibilitaServizio() {
		return disponibilitaServizio;
	}

	public void setDisponibilitaServizio(String disponibilitaServizio) {
		this.disponibilitaServizio = disponibilitaServizio;
	}

	@Column(name = "FL_PAGABILE_IRIS", nullable = false)
	public String getFlPagabileIris() {
		return flPagabileIris;
	}

	public void setFlPagabileIris(String flPagabileIris) {
		this.flPagabileIris = flPagabileIris;
	}

	@Column(name = "URL_INFO_PSP")
	public String getUrlInfoPsp() {
		return urlInfoPsp;
	}

	public void setUrlInfoPsp(String urlInfoPsp) {
		this.urlInfoPsp = urlInfoPsp;
	}

	@Column(name = "URL_INFO_CANALE")
	public String getUrlInfoCanale() {
		return urlInfoCanale;
	}

	public void setUrlInfoCanale(String urlInfoCanale) {
		this.urlInfoCanale = urlInfoCanale;
	}


	@Column(name = "DATA_PUBBLICAZIONE")
	public Timestamp getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(Timestamp dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	@Column(name = "PRIORITA")
	public String getPriorita() {
		return priorita;
	}

	public void setPriorita(String priorita) {
		this.priorita = priorita;
	}
	
	@Column(name = "FLAG_MOD_RIVERSAMENTO")
	public String getFlagModRiversamento() {
		return flagModRiversamento;
	}

	public void setFlagModRiversamento(String flagModRiversamento) {
		this.flagModRiversamento = flagModRiversamento;
	}

	@Column(name = "ID_CONTOTECNICO")
	public Long getIdContotecnico() {
		return idContotecnico;
	}

	public void setIdContotecnico(Long idContotecnico) {
		this.idContotecnico = idContotecnico;
	}

	@Column(name = "FLAG_REND_RIVERSAMENTO")
	public String getFlagRendVersamento() {
		return flagRendVersamento;
	}

	public void setFlagRendVersamento(String flagRendVersamento) {
		this.flagRendVersamento = flagRendVersamento;
	}

	@Column(name = "MODELLO_VERSAMENTO")
	public String getModelloVersamento() {
		return modelloVersamento;
	}

	public void setModelloVersamento(String modelloVersamento) {
		this.modelloVersamento = modelloVersamento;
	}

	@Column(name = "FL_STORNO_GESTITO")
	public String getFlStornoGestito() {
		return flStornoGestito;
	}

	public void setFlStornoGestito(String flStornoGestito) {
		this.flStornoGestito = flStornoGestito;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CfgGatewayPagamento [getApplicationId()=");
		builder.append(getApplicationId());
		builder.append(", getApplicationIp()=");
		builder.append(getApplicationIp());
		builder.append(", getBundleKey()=");
		builder.append(getBundleKey());
		builder.append(", getDataFineValidita()=");
		builder.append(getDataFineValidita());
		builder.append(", getDataInizioValidita()=");
		builder.append(getDataInizioValidita());
		builder.append(", getDescGateway()=");
		builder.append(getDescGateway());
		builder.append(", getDescrizione()=");
		builder.append(getDescrizione());
		builder.append(", getMaxImporto()=");
		builder.append(getMaxImporto());
		builder.append(", getMolteplicita()=");
		builder.append(getMolteplicita());
		builder.append(", getStRiga()=");
		builder.append(getStRiga());
		builder.append(", getStato()=");
		builder.append(getStato());
		builder.append(", getSystemId()=");
		builder.append(getSystemId());
		builder.append(", getSubsystemId()=");
		builder.append(getSubsystemId());
		builder.append(", getTimeout()=");
		builder.append(getTimeout());
		builder.append(", getTimeoutAup()=");
		builder.append(getTimeoutAup());
		builder.append(", getTimeoutNp()=");
		builder.append(getTimeoutNp());
		builder.append(", getCfgCommissionePagamenti()=");
		builder.append(getCfgCommissionePagamenti());
		builder.append(", getCfgCanalePagamento()=");
		builder.append(getCfgCanalePagamento());
		builder.append(", getCfgDocumentoPagamento()=");
		builder.append(getCfgDocumentoPagamento());
		builder.append(", getCfgFornitoreGateway()=");
		builder.append(getCfgFornitoreGateway());
		builder.append(", getCfgModalitaPagamento()=");
		builder.append(getCfgModalitaPagamento());
		builder.append(", getCfgStrumentoPagamento()=");
		builder.append(getCfgStrumentoPagamento());
		builder.append(", getSystemName()=");
		builder.append(getSystemName());
		builder.append(", getFlPagabileIris()=");
		builder.append(getFlPagabileIris());
		builder.append(", getUrlInfoPsp()=");
		builder.append(getUrlInfoPsp());
		builder.append(", getUrlInfoCanale()=");
		builder.append(getUrlInfoCanale());
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
