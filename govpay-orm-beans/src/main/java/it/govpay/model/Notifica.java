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

public class Notifica extends BasicModel {
	
	public Notifica() {
		
	}
	
	private static final long serialVersionUID = 1L;
	
	public enum TipoNotifica {ATTIVAZIONE, RICEVUTA}
	public enum StatoSpedizione {DA_SPEDIRE, SPEDITO}
	
	private Long id;
	private long idApplicazione;
	private Long idRpt;
	private Long idRr;
	private TipoNotifica tipo;
	private Date dataCreazione;
	private StatoSpedizione stato;
	private String descrizioneStato;
	private Long tentativiSpedizione;
	private Date dataProssimaSpedizione;
	private Date dataAggiornamento;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public TipoNotifica getTipo() {
		return tipo;
	}
	public void setTipo(TipoNotifica tipo) {
		this.tipo = tipo;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public StatoSpedizione getStato() {
		return stato;
	}
	public void setStato(StatoSpedizione stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getTentativiSpedizione() {
		return tentativiSpedizione;
	}
	public void setTentativiSpedizione(Long tentativiSpedizione) {
		this.tentativiSpedizione = tentativiSpedizione;
	}
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public Long getIdRpt() {
		return idRpt;
	}
	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}
	public Long getIdRr() {
		return idRr;
	}
	public void setIdRr(Long idRr) {
		this.idRr = idRr;	
	}
	public Date getDataProssimaSpedizione() {
		return dataProssimaSpedizione;
	}
	public void setDataProssimaSpedizione(Date dataProssimaSpedizione) {
		this.dataProssimaSpedizione = dataProssimaSpedizione;
	}
}
