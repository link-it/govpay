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

public class Tracciato {

	public enum StatoTracciatoType {
		ANNULLATO ,NUOVO ,IN_CARICAMENTO ,CARICAMENTO_OK ,CARICAMENTO_KO, STAMPATO;
	}
	
	public enum TipoTracciatoType {
		VERSAMENTI, INCASSI;
	}
	
	
	private long id;
	private Long idOperatore;
	private Long idApplicazione;
	private Date dataCaricamento;
	private Date dataUltimoAggiornamento;
	private StatoTracciatoType stato;
	private long lineaElaborazione;
	private String descrizioneStato;
	private long numLineeTotali;
	private long numOperazioniOk;
	private long numOperazioniKo;
	private String nomeFile;
	private byte[] rawDataRichiesta;
	private byte[] rawDataRisposta;
	private TipoTracciatoType tipoTracciato;
	
	public Long getIdOperatore() {
		return idOperatore;
	}
	public void setIdOperatore(Long idOperatore) {
		this.idOperatore = idOperatore;
	}
	public Date getDataCaricamento() {
		return dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public StatoTracciatoType getStato() {
		return stato;
	}
	public void setStato(StatoTracciatoType stato) {
		this.stato = stato;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public long getNumLineeTotali() {
		return numLineeTotali;
	}
	public void setNumLineeTotali(long numLineeTotali) {
		this.numLineeTotali = numLineeTotali;
	}
	public long getNumOperazioniOk() {
		return numOperazioniOk;
	}
	public void setNumOperazioniOk(long numOperazioniOk) {
		this.numOperazioniOk = numOperazioniOk;
	}
	public long getNumOperazioniKo() {
		return numOperazioniKo;
	}
	public void setNumOperazioniKo(long numOperazioniKo) {
		this.numOperazioniKo = numOperazioniKo;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public byte[] getRawDataRichiesta() {
		return rawDataRichiesta;
	}
	public void setRawDataRichiesta(byte[] rawDataRichiesta) {
		this.rawDataRichiesta = rawDataRichiesta;
	}
	public byte[] getRawDataRisposta() {
		return rawDataRisposta;
	}
	public void setRawDataRisposta(byte[] rawDataRisposta) {
		this.rawDataRisposta = rawDataRisposta;
	}
	public Long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public TipoTracciatoType getTipoTracciato() {
		return tipoTracciato;
	}
	public void setTipoTracciato(TipoTracciatoType tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
	}
}
