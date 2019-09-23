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

public class Promemoria extends BasicModel {
	
	public Promemoria() {
		
	}
	
	private static final long serialVersionUID = 1L;
	
	public enum TipoPromemoria { AVVISO, RICEVUTA}
	public enum StatoSpedizione {DA_SPEDIRE, SPEDITO, ANNULLATO, FALLITO}
	
	private Long id;
	private long idVersamento;
	private Long idRpt;
	private TipoPromemoria tipo;
	private Date dataCreazione;
	private StatoSpedizione stato;
	private String descrizioneStato;
	private Long tentativiSpedizione;
	private Date dataProssimaSpedizione;
	private Date dataAggiornamento;
	private String destinatarioTo;
	private String destinatarioCc;
	private String oggetto;
	private String messaggio;
	private boolean allegaPdf;
	private String contentType;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public TipoPromemoria getTipo() {
		return tipo;
	}
	public void setTipo(TipoPromemoria tipo) {
		this.tipo = tipo;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
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
	public boolean isAllegaPdf() {
		return allegaPdf;
	}
	public void setAllegaPdf(boolean allegaPdf) {
		this.allegaPdf = allegaPdf;
	}
	public Long getIdRpt() {
		return idRpt;
	}
	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDestinatarioTo() {
		return destinatarioTo;
	}
	public void setDestinatarioTo(String destinatarioTo) {
		this.destinatarioTo = destinatarioTo;
	}
	public String getDestinatarioCc() {
		return destinatarioCc;
	}
	public void setDestinatarioCc(String destinatarioCc) {
		this.destinatarioCc = destinatarioCc;
	}
}
