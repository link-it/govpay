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
package it.govpay.model;

import java.util.Date;

public class NotificaAppIo extends BasicModel {
	
	public NotificaAppIo() {
		
	}
	
	private static final long serialVersionUID = 1L;
	
	public enum TipoNotifica { AVVISO, SCADENZA, RICEVUTA }
	
	public enum StatoSpedizione {DA_SPEDIRE, SPEDITO, ANNULLATA}
	
	public enum StatoMessaggio {ACCEPTED, THROTTLED, FAILED, PROCESSED, REJECTED}
	
	private Long id;
	private long idVersamento;
	private long idTipoVersamentoDominio;
	
	private String codVersamentoEnte;
	private String codApplicazione;
	private String codDominio;
	private String iuv;
	private String debitoreIdentificativo;
	
	private TipoNotifica tipo;
	private Date dataCreazione;
	private StatoSpedizione stato;
	private String descrizioneStato;
	private Long tentativiSpedizione;
	private Date dataProssimaSpedizione;
	private Date dataAggiornamento;
	
	// dati ricevuti da APP IO
	private String idMessaggio;
	private StatoMessaggio statoMessaggio;
	private Long idRpt;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TipoNotifica getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoNotifica tipo) {
		this.tipo = tipo;
	}
	public Date getDataCreazione() {
		return this.dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public StatoSpedizione getStato() {
		return this.stato;
	}
	public void setStato(StatoSpedizione stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getTentativiSpedizione() {
		return this.tentativiSpedizione;
	}
	public void setTentativiSpedizione(Long tentativiSpedizione) {
		this.tentativiSpedizione = tentativiSpedizione;
	}
	public Date getDataAggiornamento() {
		return this.dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public Date getDataProssimaSpedizione() {
		return this.dataProssimaSpedizione;
	}
	public void setDataProssimaSpedizione(Date dataProssimaSpedizione) {
		this.dataProssimaSpedizione = dataProssimaSpedizione;
	}
	public long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public long getIdTipoVersamentoDominio() {
		return idTipoVersamentoDominio;
	}
	public void setIdTipoVersamentoDominio(long idTipoVersamentoDominio) {
		this.idTipoVersamentoDominio = idTipoVersamentoDominio;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getDebitoreIdentificativo() {
		return debitoreIdentificativo;
	}
	public void setDebitoreIdentificativo(String debitoreIdentificativo) {
		this.debitoreIdentificativo = debitoreIdentificativo;
	}
	public String getIdMessaggio() {
		return idMessaggio;
	}
	public void setIdMessaggio(String idMessaggio) {
		this.idMessaggio = idMessaggio;
	}
	public StatoMessaggio getStatoMessaggio() {
		return statoMessaggio;
	}
	public void setStatoMessaggio(StatoMessaggio statoMessaggio) {
		this.statoMessaggio = statoMessaggio;
	}
	public Long getIdRpt() {
		return idRpt;
	}
	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}
}
