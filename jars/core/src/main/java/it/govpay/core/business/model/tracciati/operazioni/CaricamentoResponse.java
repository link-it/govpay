/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.model.tracciati.operazioni;

import it.govpay.core.beans.tracciati.Avviso;

public class CaricamentoResponse extends AbstractOperazioneResponse {

	public static final String ESITO_ADD_OK = "ADD_OK";
	public static final String ESITO_ADD_KO = "ADD_KO";

	public CaricamentoResponse() { 	super(); }
	private String iuv;
	private byte[] qrCode;
	private byte[] barCode;
	private Avviso avviso;

	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public byte[] getQrCode() {
		return this.qrCode;
	}
	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}
	public byte[] getBarCode() {
		return this.barCode;
	}
	public void setBarCode(byte[] barCode) {
		this.barCode = barCode;
	}
	public Avviso getAvviso() {
		return this.avviso;
	}
	public void setAvviso(Avviso avviso) {
		this.avviso = avviso;
	}

	@Override
	public Object getDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO:
			return this.getFaultBean(); 
		case ESEGUITO_OK:
			return this.getAvviso();
		case NON_VALIDO:
		default:
			break;
		}
		
		return null;
	}
}
