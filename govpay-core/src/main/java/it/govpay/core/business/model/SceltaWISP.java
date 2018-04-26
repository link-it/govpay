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

import it.govpay.core.exceptions.NdpException.FaultNodo;


public class SceltaWISP {
	private String codPsp;
	private String codCanale;
	private String tipoVersamento;
	private boolean sceltaEffettuata;
	private boolean pagaDopo;
	private FaultNodo fault;
	
	public boolean isSceltaEffettuata() {
		return sceltaEffettuata;
	}
	public void setSceltaEffettuata(boolean sceltaEffettuata) {
		this.sceltaEffettuata = sceltaEffettuata;
	}
	public boolean isPagaDopo() {
		return pagaDopo;
	}
	public void setPagaDopo(boolean pagaDopo) {
		this.pagaDopo = pagaDopo;
	}
	public FaultNodo getFault() {
		return fault;
	}
	public void setFault(FaultNodo fault) {
		this.fault = fault;
	}
	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getCodCanale() {
		return codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

}
