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

import java.util.Date;
import java.util.List;

public class Rr extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	
	public enum Stato {
		RR_DA_INVIARE_A_NODO,
		RR_ERRORE_INVIO_A_NODO,
		RR_RICEVUTA_NODO, 
		RR_ESITATO_NODO, 
	}
	
	private Long id;
	private long idRt;
	private Long idTracciatoXML;
	private String codMsgRevoca;
	private Date dataOraMsgRevoca;
	private double importoTotaleRevocato;
	private Stato stato;
	private String descrizioneStato;
	private Date dataOraCreazione;
	private List<SingolaRevoca> singolaRevocaList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDataOraMsgRevoca() {
		return dataOraMsgRevoca;
	}
	public void setDataOraMsgRevoca(Date dataOraMsgRevoca) {
		this.dataOraMsgRevoca = dataOraMsgRevoca;
	}
	public String getCodMsgRevoca() {
		return codMsgRevoca;
	}
	public void setCodMsgRevoca(String codMsgRevoca) {
		this.codMsgRevoca = codMsgRevoca;
	}
	public Stato getStato() {
		return stato;
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getIdTracciatoXML() {
		return idTracciatoXML;
	}
	public void setIdTracciatoXML(Long idTracciatoXML) {
		this.idTracciatoXML = idTracciatoXML;
	}
	
	@Override
	public boolean equals(Object obj) {
		Rr rr = null;
		if(obj instanceof Rr) {
			rr = (Rr) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(dataOraMsgRevoca, rr.getDataOraMsgRevoca()) &&
				equals(dataOraCreazione, rr.getDataOraCreazione()) &&
				equals(codMsgRevoca, rr.getCodMsgRevoca()) &&
				equals(idRt, rr.getIdRt()) &&
				equals(importoTotaleRevocato, rr.getImportoTotaleRevocato()) &&
				equals(stato, rr.getStato()) &&
				equals(descrizioneStato, rr.getDescrizioneStato()) &&
				equals(singolaRevocaList, rr.getSingolaRevocaList()) &&
				equals(idTracciatoXML, rr.getIdTracciatoXML());
		
		return equal;
	}
	public Date getDataOraCreazione() {
		return dataOraCreazione;
	}
	public void setDataOraCreazione(Date dataOraCreazione) {
		this.dataOraCreazione = dataOraCreazione;
	}
	public long getIdRt() {
		return idRt;
	}
	public void setIdRt(long idRt) {
		this.idRt = idRt;
	}
	public double getImportoTotaleRevocato() {
		return importoTotaleRevocato;
	}
	public void setImportoTotaleRevocato(double importoTotaleRevocato) {
		this.importoTotaleRevocato = importoTotaleRevocato;
	}
	public List<SingolaRevoca> getSingolaRevocaList() {
		return singolaRevocaList;
	}
	public void setSingolaRevocaList(List<SingolaRevoca> singolaRevocaList) {
		this.singolaRevocaList = singolaRevocaList;
	}

	
}
