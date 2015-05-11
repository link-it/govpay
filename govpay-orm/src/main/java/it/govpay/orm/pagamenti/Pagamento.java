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
package it.govpay.orm.pagamenti;

import it.govpay.orm.BaseEntity;
import it.govpay.orm.posizionedebitoria.Condizione;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pagamenti")
public class Pagamento extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String cdTrbEnte;
	private String coPagante;
	private Date dataAccreditoContotecnico;

	private Date dataAccreditoEnte;
	private String distinta = "DISTINTA";
	private Date dtScadenza;
	private DistintaPagamento distintaPagamento;
	private String idEnte;
	private String idPendenza;
	private String idPendenzaente;
	private String idTributo;
	private BigDecimal imPagato;
	private Timestamp notificaEseguito;
	private Timestamp notificaIncasso;
	private Timestamp notificaRegolato;
	private String stPagamento;
	private String stRiga;
	private String statoNotifica;
	private String tiDebito;
	private String tiPagamento;
	private String tipospontaneo;
	private Timestamp tsDecorrenza;
	private Timestamp tsOrdine;
	private Timestamp tsStorno;
	private Condizione condPagamento;
	private String flagIncasso;
	private Long idDocumentoRepository;
	private String idRiscossionePSP;
	private String notePagamento;

	/*** Auto Generated Identity Property ***/
	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CD_TRB_ENTE")
	public String getCdTrbEnte() {
		return this.cdTrbEnte;
	}

	public void setCdTrbEnte(String cdTrbEnte) {
		this.cdTrbEnte = cdTrbEnte;
	}

	@Column(name = "CO_PAGANTE")
	public String getCoPagante() {
		return this.coPagante;
	}

	public void setCoPagante(String coPagante) {
		this.coPagante = coPagante;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ACCREDITO_CONTOTECNICO")
	public Date getDataAccreditoContotecnico() {
		return this.dataAccreditoContotecnico;
	}

	public void setDataAccreditoContotecnico(Date dataAccreditoContotecnico) {
		this.dataAccreditoContotecnico = dataAccreditoContotecnico;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ACCREDITO_ENTE")
	public Date getDataAccreditoEnte() {
		return this.dataAccreditoEnte;
	}

	public void setDataAccreditoEnte(Date dataAccreditoEnte) {
		this.dataAccreditoEnte = dataAccreditoEnte;
	}

	@Column(name = "DISTINTA")
	public String getDistinta() {
		return this.distinta;
	}

	public void setDistinta(String distinta) {
		this.distinta = distinta;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_SCADENZA")
	public Date getDtScadenza() {
		return this.dtScadenza;
	}

	public void setDtScadenza(Date dtScadenza) {
		this.dtScadenza = dtScadenza;
	}

	@ManyToOne(targetEntity = DistintaPagamento.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISTINTE_PAGAMENTO")
	public DistintaPagamento getDistintaPagamento() {
		return this.distintaPagamento;
	}

	public void setDistintaPagamento(DistintaPagamento distintaPagamento) {
		this.distintaPagamento = distintaPagamento;
	}

	@Column(name = "ID_ENTE")
	public String getIdEnte() {
		return this.idEnte;
	}

	public void setIdEnte(String idEnte) {
		this.idEnte = idEnte;
	}

	@Column(name = "ID_PENDENZA")
	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	@Column(name = "ID_PENDENZAENTE")
	public String getIdPendenzaente() {
		return this.idPendenzaente;
	}

	public void setIdPendenzaente(String idPendenzaente) {
		this.idPendenzaente = idPendenzaente;
	}

	@Column(name = "ID_TRIBUTO")
	public String getIdTributo() {
		return this.idTributo;
	}

	public void setIdTributo(String idTributo) {
		this.idTributo = idTributo;
	}

	@Column(name = "IM_PAGATO")
	public BigDecimal getImPagato() {
		return this.imPagato;
	}

	public void setImPagato(BigDecimal imPagato) {
		this.imPagato = imPagato;
	}

	@Column(name = "NOTIFICA_ESEGUITO")
	public Timestamp getNotificaEseguito() {
		return this.notificaEseguito;
	}

	public void setNotificaEseguito(Timestamp notificaEseguito) {
		this.notificaEseguito = notificaEseguito;
	}

	@Column(name = "NOTIFICA_INCASSO")
	public Timestamp getNotificaIncasso() {
		return this.notificaIncasso;
	}

	public void setNotificaIncasso(Timestamp notificaIncasso) {
		this.notificaIncasso = notificaIncasso;
	}

	@Column(name = "NOTIFICA_REGOLATO")
	public Timestamp getNotificaRegolato() {
		return this.notificaRegolato;
	}

	public void setNotificaRegolato(Timestamp notificaRegolato) {
		this.notificaRegolato = notificaRegolato;
	}

	@Column(name = "ST_PAGAMENTO")
	public String getStPagamento() {
		return this.stPagamento;
	}

	public void setStPagamento(String stPagamento) {
		this.stPagamento = stPagamento;
	}

	@Column(name = "ST_RIGA")
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(name = "STATO_NOTIFICA")
	public String getStatoNotifica() {
		return this.statoNotifica;
	}

	public void setStatoNotifica(String statoNotifica) {
		this.statoNotifica = statoNotifica;
	}

	@Column(name = "TI_DEBITO")
	public String getTiDebito() {
		return this.tiDebito;
	}

	public void setTiDebito(String tiDebito) {
		this.tiDebito = tiDebito;
	}

	@Column(name = "TI_PAGAMENTO")
	public String getTiPagamento() {
		return this.tiPagamento;
	}

	public void setTiPagamento(String tiPagamento) {
		this.tiPagamento = tiPagamento;
	}

	@Column(name = "TIPOSPONTANEO")
	public String getTipospontaneo() {
		return this.tipospontaneo;
	}

	public void setTipospontaneo(String tipospontaneo) {
		this.tipospontaneo = tipospontaneo;
	}

	@Column(name = "TS_DECORRENZA")
	public Timestamp getTsDecorrenza() {
		return this.tsDecorrenza;
	}

	public void setTsDecorrenza(Timestamp tsDecorrenza) {
		this.tsDecorrenza = tsDecorrenza;
	}

	@Column(name = "TS_ORDINE")
	public Timestamp getTsOrdine() {
		return this.tsOrdine;
	}

	public void setTsOrdine(Timestamp tsOrdine) {
		this.tsOrdine = tsOrdine;
	}

	@ManyToOne(targetEntity = Condizione.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONDIZIONE", nullable = true)
	public Condizione getCondPagamento() {
		return condPagamento;
	}

	public void setCondPagamento(Condizione condPagamento) {
		this.condPagamento = condPagamento;
	}

	@Column(name = "FLAG_INCASSO")
	public String getFlagIncasso() {
		return flagIncasso;
	}

	public void setFlagIncasso(String flagIncasso) {
		this.flagIncasso = flagIncasso;
	}

	@Column(name = "ID_DOCUMENTO_REPOSITORY")
	public Long getIdDocumentoRepository() {
		return idDocumentoRepository;
	}

	public void setIdDocumentoRepository(Long idDocumentoRepository) {
		this.idDocumentoRepository = idDocumentoRepository;
	}

	@Column(name = "ID_RISCOSSIONE_PSP")
	public String getIdRiscossionePSP() {
		return idRiscossionePSP;
	}

	public void setIdRiscossionePSP(String idRiscossionePSP) {
		this.idRiscossionePSP = idRiscossionePSP;
	}

	@Column(name = "NOTE_PAGAMENTO")
	public String getNotePagamento() {
		return notePagamento;
	}

	public void setNotePagamento(String notePagamento) {
		this.notePagamento = notePagamento;
	}


	@Column(name = "TS_STORNO")
	public Timestamp getTsStorno() {
		return tsStorno;
	}

	public void setTsStorno(Timestamp tsStorno) {
		this.tsStorno = tsStorno;
	}


}
