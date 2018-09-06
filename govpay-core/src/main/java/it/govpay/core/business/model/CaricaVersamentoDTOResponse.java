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

package it.govpay.core.business.model;

public class CaricaVersamentoDTOResponse {
	
	private String codApplicazione;
	private String codVersamentoEnte;
	private String codDominio;
	private String iuv;
	private String numeroAvviso;
	private byte[] qrCode;
	private byte[] barCode;

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String value) {
		this.codApplicazione = value;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String value) {
		this.codVersamentoEnte = value;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String value) {
		this.codDominio = value;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String value) {
		this.iuv = value;
	}

	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}

	public void setNumeroAvviso(String value) {
		this.numeroAvviso = value;
	}

	public byte[] getQrCode() {
		return this.qrCode;
	}

	public void setQrCode(byte[] value) {
		this.qrCode = value;
	}

	public byte[] getBarCode() {
		return this.barCode;
	}

	public void setBarCode(byte[] value) {
		this.barCode = value;
	}
	
	

}
