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
package it.govpay.model;

public class Psp extends BasicModel {	
	
	private static final long serialVersionUID = 1L;
	private String codPsp;
	private String ragioneSociale;
	private String codIntermediarioPsp;
	
	public Psp(String codIntermediarioPsp, String codPsp, String ragioneSociale) {
		this.codPsp = codPsp;
		this.ragioneSociale = ragioneSociale;
		this.codIntermediarioPsp = codIntermediarioPsp;
	}
	
	public String getCodPsp() {
		return this.codPsp;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public String getCodIntermediarioPsp() {
		return this.codIntermediarioPsp;
	}

}
