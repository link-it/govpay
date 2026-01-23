/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPTRisposta;


public class Risposta  {

	private String esito;
	private FaultBean faultBean;
	private String url;

	public Risposta(NodoInviaRPTRisposta r) {
		this.esito = r.getEsito();
		this.url = r.getUrl();
		this.faultBean = r.getFault();
	}

	public String getEsito() {
		return this.esito;
	}

	public String getUrl() {
		return this.url;
	}

	public FaultBean getFaultBean() {
		return this.faultBean;
	}

	public String getLog() {
		StringBuilder log = new StringBuilder("Ricevuto esito " + this.esito);

		if(this.faultBean != null) {
			log.append(" con FaultBean ");
			log.append("\n" + toString(this.faultBean));
		}

		return log.toString();
	}

	private String toString(FaultBean faultBean) {
		StringBuilder sb = new StringBuilder();
		if(faultBean != null) {

			if(faultBean.getSerial() != null)
				sb.append("#" + faultBean.getSerial() + " ");

			sb.append("["+faultBean.getFaultCode()+"]");

			if(faultBean.getFaultString() != null)
				sb.append(" " + faultBean.getFaultString());

			if(faultBean.getDescription() != null)
				sb.append(": " +  faultBean.getDescription() + " ");
		}
		return sb.toString();
	}

}
