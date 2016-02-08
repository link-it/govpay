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

import java.util.Arrays;


public class TabellaControparti extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id;
	private long idDominio;
	private java.lang.String idFlusso;
	private java.util.Date dataOraPubblicazione;
	private java.util.Date dataOraInizioValidita;
	private byte[] xml;
	
	public TabellaControparti() {}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public long getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}


	public java.lang.String getIdFlusso() {
		return idFlusso;
	}


	public void setIdFlusso(java.lang.String idFlusso) {
		this.idFlusso = idFlusso;
	}


	public java.util.Date getDataOraPubblicazione() {
		return dataOraPubblicazione;
	}


	public void setDataOraPubblicazione(java.util.Date dataOraPubblicazione) {
		this.dataOraPubblicazione = dataOraPubblicazione;
	}


	public java.util.Date getDataOraInizioValidita() {
		return dataOraInizioValidita;
	}


	public void setDataOraInizioValidita(java.util.Date dataOraInizioValidita) {
		this.dataOraInizioValidita = dataOraInizioValidita;
	}


	public byte[] getXml() {
		return xml;
	}


	public void setXml(byte[] xml) {
		this.xml = xml;
	}


	@Override
	public boolean equals(Object obj) {
		TabellaControparti tabellaControparti = null;
		if(obj instanceof TabellaControparti) {
			tabellaControparti = (TabellaControparti) obj;
		} else {
			return false;
		}

		boolean equal =
				equals(idDominio, tabellaControparti.getIdDominio()) &&
				equals(idFlusso, tabellaControparti.getIdFlusso()) &&
				equals(dataOraPubblicazione, tabellaControparti.getDataOraPubblicazione()) &&
				equals(dataOraInizioValidita, tabellaControparti.getDataOraInizioValidita()) &&
				Arrays.equals(xml, tabellaControparti.getXml());

		return equal;
	}
}
