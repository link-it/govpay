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


public class MediaRilevamento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idSLA;
	private long idApplicazione;
	private java.util.Date dataOsservazione;
	private long numRilevamentiA;
	private double percentualeA;
	private long numRilevamentiB;
	private double percentualeB;
	private long numRilevamentiOver;

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

	public java.util.Date getDataOsservazione() {
		return dataOsservazione;
	}

	public void setDataOsservazione(java.util.Date dataOsservazione) {
		this.dataOsservazione = dataOsservazione;
	}

	public long getNumRilevamentiA() {
		return numRilevamentiA;
	}

	public void setNumRilevamentiA(long numRilevamentiA) {
		this.numRilevamentiA = numRilevamentiA;
	}

	public double getPercentualeA() {
		return percentualeA;
	}

	public void setPercentualeA(double percentualeA) {
		this.percentualeA = percentualeA;
	}

	public long getNumRilevamentiB() {
		return numRilevamentiB;
	}

	public void setNumRilevamentiB(long numRilevamentiB) {
		this.numRilevamentiB = numRilevamentiB;
	}

	public double getPercentualeB() {
		return percentualeB;
	}

	public void setPercentualeB(double percentualeB) {
		this.percentualeB = percentualeB;
	}

	public long getNumRilevamentiOver() {
		return numRilevamentiOver;
	}

	public void setNumRilevamentiOver(long numRilevamentiOver) {
		this.numRilevamentiOver = numRilevamentiOver;
	}

	@Override
	public boolean equals(Object obj) {
		MediaRilevamento sv = null;
		if(obj instanceof MediaRilevamento) {
			sv = (MediaRilevamento) obj;
		} else {
			return false;
		}
		boolean equal = 
				equals(idSLA, sv.getIdSLA()) &&
				equals(idApplicazione, sv.getIdApplicazione()) &&
				equals(dataOsservazione, sv.getDataOsservazione()) &&
				equals(numRilevamentiA, sv.getNumRilevamentiA()) &&
				equals(percentualeA, sv.getPercentualeA()) &&
				equals(numRilevamentiB, sv.getNumRilevamentiB()) &&
				equals(percentualeB, sv.getPercentualeB()) &&
				equals(numRilevamentiOver, sv.getNumRilevamentiOver());
		return equal;
	}
	
}
