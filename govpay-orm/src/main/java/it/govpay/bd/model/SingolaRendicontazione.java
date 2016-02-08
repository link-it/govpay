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


public class SingolaRendicontazione extends BasicModel{
	private static final long serialVersionUID = 1L;


	private Long id;
	private long idSingoloVersamento;
	private double singoloImporto;
	private String iur;
	private String iuv;
	private int codiceEsito;
	private Date dataEsito;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		SingolaRendicontazione singolaRendicontazione = null;
		if(obj instanceof SingolaRendicontazione) {
			singolaRendicontazione = (SingolaRendicontazione) obj;
		} else {
			return false;
		}

		boolean equal =
				equals(idSingoloVersamento, singolaRendicontazione.getIdSingoloVersamento()) &&
				equals(iuv, singolaRendicontazione.getIuv()) &&
				equals(iur, singolaRendicontazione.getIur()) &&
				equals(singoloImporto, singolaRendicontazione.getSingoloImporto()) &&
				codiceEsito == singolaRendicontazione.getCodiceEsito() &&
				equals(dataEsito, singolaRendicontazione.getDataEsito());

		return equal;
	}
	public int getCodiceEsito() {
		return codiceEsito;
	}
	public void setCodiceEsito(int codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public double getSingoloImporto() {
		return singoloImporto;
	}
	public void setSingoloImporto(double singoloImporto) {
		this.singoloImporto = singoloImporto;
	}
	public long getIdSingoloVersamento() {
		return idSingoloVersamento;
	}
	public void setIdSingoloVersamento(long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public Date getDataEsito() {
		return dataEsito;
	}
	public void setDataEsito(Date dataEsito) {
		this.dataEsito = dataEsito;
	}


}
