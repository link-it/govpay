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

public class Rilevamento extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id;
	private long idSLA;
	private long idApplicazione;
	private long idEventoIniziale;
	private long idEventoFinale;
	private Date dataRilevamento;
	private long durata;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public long getIdSLA() {
		return idSLA;
	}


	public void setIdSLA(long idSLA) {
		this.idSLA = idSLA;
	}


	public long getIdApplicazione() {
		return idApplicazione;
	}


	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}


	public long getIdEventoIniziale() {
		return idEventoIniziale;
	}


	public void setIdEventoIniziale(long idEventoIniziale) {
		this.idEventoIniziale = idEventoIniziale;
	}


	public long getIdEventoFinale() {
		return idEventoFinale;
	}


	public void setIdEventoFinale(long idEventoFinale) {
		this.idEventoFinale = idEventoFinale;
	}


	public Date getDataRilevamento() {
		return dataRilevamento;
	}


	public void setDataRilevamento(Date dataRilevamento) {
		this.dataRilevamento = dataRilevamento;
	}


	public long getDurata() {
		return durata;
	}


	public void setDurata(long durata) {
		this.durata = durata;
	}


	@Override
	public boolean equals(Object obj) {
		Rilevamento evento = null;
		if(obj instanceof Rilevamento) {
			evento = (Rilevamento) obj;
		} else {
			return false;
		}

		boolean equal = 
				equals(idSLA, evento.getIdSLA()) &&
				equals(idApplicazione, evento.getIdApplicazione()) &&
				equals(idEventoIniziale, evento.getIdEventoIniziale()) &&
				equals(idEventoFinale, evento.getIdEventoFinale()) &&
				equals(dataRilevamento, evento.getDataRilevamento()) &&
				equals(durata, evento.getDurata());

		return equal;
	}


}
