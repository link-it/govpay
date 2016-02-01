/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fr extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	public enum StatoFr {
		ACQUISITA,
		ACCETTATA,
		RIFIUTATA
	}
	
	private Long id;
	private Long idTracciatoXML;
	private long idPsp;
	private long idDominio;
	
	private String codFlusso;
	private int annoRiferimento;
	private Date dataOraFlusso;
	
	private String iur;
	private Date dataRegolamento;
	private long numeroPagamenti;
	private double importoTotalePagamenti;
	private StatoFr stato;
	private String descrizioneStato;
	private List<SingolaRendicontazione> singolaRendicontazioneList;
	
	public Fr() {
		this.singolaRendicontazioneList = new ArrayList<SingolaRendicontazione>();
	}
	
	public int getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	public long getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(long numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public StatoFr getStato() {
		return stato;
	}
	public void setStato(StatoFr stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public long getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(long idPsp) {
		this.idPsp = idPsp;
	}
	public Date getDataOraFlusso() {
		return dataOraFlusso;
	}
	public void setDataOraFlusso(Date dataOraFlusso) {
		this.dataOraFlusso = dataOraFlusso;
	}
	public Long getIdTracciatoXML() {
		return idTracciatoXML;
	}
	public void setIdTracciatoXML(Long idTracciatoXML) {
		this.idTracciatoXML = idTracciatoXML;
	}
	public double getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(double importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
	
	@Override
	public boolean equals(Object obj) {
		Fr rpt = null;
		if(obj instanceof Fr) {
			rpt = (Fr) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(codFlusso, rpt.getCodFlusso()) &&
				equals(dataOraFlusso, rpt.getDataOraFlusso()) &&
				equals(dataRegolamento, rpt.getDataRegolamento()) &&
				equals(idTracciatoXML, rpt.getIdTracciatoXML()) &&
				equals(iur, rpt.getIur()) &&
				equals(annoRiferimento, rpt.getAnnoRiferimento()) &&
				equals(numeroPagamenti, rpt.getNumeroPagamenti()) &&
				equals(stato, rpt.getStato()) &&
				equals(descrizioneStato, rpt.getDescrizioneStato()) &&
				idPsp == rpt.getIdPsp() &&
				importoTotalePagamenti == rpt.getImportoTotalePagamenti() &&
				equals(getSingolaRendicontazioneList(), rpt.getSingolaRendicontazioneList());
		
		return equal;
	}
	public Date getDataRegolamento() {
		return dataRegolamento;
	}
	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}
	public List<SingolaRendicontazione> getSingolaRendicontazioneList() {
		return singolaRendicontazioneList;
	}
	public void setSingolaRendicontazioneList(
			List<SingolaRendicontazione> singolaRendicontazioneList) {
		this.singolaRendicontazioneList = singolaRendicontazioneList;
	}

	public long getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}

	
}
