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
package it.govpay.ndp.model.impl;

import java.util.Date;

import it.govpay.ndp.model.DocumentoModel;

public class FRModel extends DocumentoModel {
	
	private String idFlusso; 
	private String idPsp; 
	private String idDominio;
	private Date dataFlusso;
	private byte[] flusso;
	
	public FRModel(String idDominio, String idPsp, String idFlusso, Date dataFlusso, byte[] flusso) {
		super(DocumentoModel.TipoDocumento.FR, idDominio, flusso);
		this.idDominio = idDominio;
		this.idFlusso = idFlusso;
		this.idPsp = idPsp;
		this.dataFlusso = dataFlusso;
		this.flusso = flusso;
	}

	public enum TipoDocumento {
		FR;
	}

	public String getIdFlusso() {
		return idFlusso;
	}

	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}

	public String getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}

	public Date getDataFlusso() {
		return dataFlusso;
	}

	public void setDataFlusso(Date dataFlusso) {
		this.dataFlusso = dataFlusso;
	}

	public byte[] getFlusso() {
		return flusso;
	}

	public void setFlusso(byte[] flusso) {
		this.flusso = flusso;
	}
	
	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
}
