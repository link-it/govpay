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

public class Er extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	
	public enum Stato {
		ER_ACCETTATA,
		ER_RIFIUTATA
	}
	
	private Long id;
	private long idRr;
	private Long idTracciatoXML;
	private String codMsgEsito;
	private Date dataOraMsgEsito;
	private double importoTotaleRevocato;
	private Stato stato;
	private String descrizioneStato;
	private Date dataOraCreazione;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDataOraMsgEsito() {
		return dataOraMsgEsito;
	}
	public void setDataOraMsgEsito(Date dataOraMsgEsito) {
		this.dataOraMsgEsito = dataOraMsgEsito;
	}
	public String getCodMsgEsito() {
		return codMsgEsito;
	}
	public void setCodMsgEsito(String codMsgEsito) {
		this.codMsgEsito = codMsgEsito;
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
		Er er = null;
		if(obj instanceof Er) {
			er = (Er) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(dataOraMsgEsito, er.getDataOraMsgEsito()) &&
				equals(dataOraCreazione, er.getDataOraCreazione()) &&
				equals(codMsgEsito, er.getCodMsgEsito()) &&
				equals(idRr, er.getIdRr()) &&
				equals(importoTotaleRevocato, er.getImportoTotaleRevocato()) &&
				equals(stato, er.getStato()) &&
				equals(descrizioneStato, er.getDescrizioneStato()) &&
				equals(idTracciatoXML, er.getIdTracciatoXML());
		
		return equal;
	}
	public Date getDataOraCreazione() {
		return dataOraCreazione;
	}
	public void setDataOraCreazione(Date dataOraCreazione) {
		this.dataOraCreazione = dataOraCreazione;
	}
	public long getIdRr() {
		return idRr;
	}
	public void setIdRr(long idRr) {
		this.idRr = idRr;
	}
	public double getImportoTotaleRevocato() {
		return importoTotaleRevocato;
	}
	public void setImportoTotaleRevocato(double importoTotaleRevocato) {
		this.importoTotaleRevocato = importoTotaleRevocato;
	}

	
}
