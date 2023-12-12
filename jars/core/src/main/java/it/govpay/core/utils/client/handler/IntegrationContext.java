/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.client.handler;

import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.model.Applicazione;
import it.govpay.model.Intermediario;


public class IntegrationContext {

	private byte[] msg;
	private Applicazione applicazione;
	private Intermediario intermediario;
	private TipoConnettore tipoConnettore;
	private TipoDestinatario tipoDestinatario;
	
	public byte[] getMsg() {
		return this.msg;
	}
	public void setMsg(byte[] msg) {
		this.msg = msg;
	}
	public Applicazione getApplicazione() {
		return this.applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public Intermediario getIntermediario() {
		return intermediario;
	}
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}
	public TipoConnettore getTipoConnettore() {
		return tipoConnettore;
	}
	public void setTipoConnettore(TipoConnettore tipoConnettore) {
		this.tipoConnettore = tipoConnettore;
	}
	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}
	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}
}
