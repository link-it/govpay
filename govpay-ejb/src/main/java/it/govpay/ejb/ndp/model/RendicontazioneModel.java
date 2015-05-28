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
package it.govpay.ejb.ndp.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RendicontazioneModel {
	
	private boolean valido;
	private byte[] flusso;
	private String mittente;
	private String ricevente;
	private String descErrore;
	private Date dataRendicontazione;
	private Date dataRegolamento;
	private String stato;
	private String idRegolamento;
	private BigDecimal importoRendicontato;
	private List<RendicontazionePagamentoModel> rendicontazioniPagamento;
	
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}
	public byte[] getFlusso() {
		return flusso;
	}
	public void setFlusso(byte[] flusso) {
		this.flusso = flusso;
	}
	public Date getDataRendicontazione() {
		return dataRendicontazione;
	}
	public void setDataRendicontazione(Date dataRendicontazione) {
		this.dataRendicontazione = dataRendicontazione;
	}
	public Date getDataRegolamento() {
		return dataRegolamento;
	}
	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getIdRegolamento() {
		return idRegolamento;
	}
	public void setIdRegolamento(String idRegolamento) {
		this.idRegolamento = idRegolamento;
	}
	public BigDecimal getImportoRendicontato() {
		return importoRendicontato;
	}
	public void setImportoRendicontato(BigDecimal importoRendicontato) {
		this.importoRendicontato = importoRendicontato;
	}
	public List<RendicontazionePagamentoModel> getRendicontazioniPagamento() {
		return rendicontazioniPagamento;
	}
	public void setRendicontazioniPagamento(
			List<RendicontazionePagamentoModel> rendicontazioniPagamento) {
		this.rendicontazioniPagamento = rendicontazioniPagamento;
	}
	public String getMittente() {
		return mittente;
	}
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	public String getRicevente() {
		return ricevente;
	}
	public void setRicevente(String ricevente) {
		this.ricevente = ricevente;
	}
	public String getDescErrore() {
		return descErrore;
	}
	public void setDescErrore(String descErrore) {
		this.descErrore = descErrore;
	}
	
}
