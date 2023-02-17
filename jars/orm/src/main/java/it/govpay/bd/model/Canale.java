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
package it.govpay.bd.model;

import it.govpay.model.exception.CodificaInesistenteException;

public class Canale extends it.govpay.model.Canale {
	
	private static final long serialVersionUID = 1L;
	public static final Canale canaleUniversale = new Canale(it.govpay.model.Rpt.codIntermediarioPspWISP20, it.govpay.model.Rpt.codPspWISP20, it.govpay.model.Rpt.codCanaleWISP20,	it.govpay.model.Rpt.tipoVersamentoWISP20, null);

	public Canale(Psp psp, String codCanale, String tipoVersamento) throws CodificaInesistenteException {
		this(psp, codCanale, TipoVersamento.toEnum(tipoVersamento));
	}
	
	public Canale(String codIntermediarioPsp, String codPsp, String codCanale, TipoVersamento tipoVersamento, String ragioneSociale) {
		this(new Psp(codIntermediarioPsp, codPsp, ragioneSociale), codCanale, tipoVersamento);
	}
	
	public Canale(String codIntermediarioPsp, String codPsp, String codCanale, String tipoVersamento, String ragioneSociale) throws CodificaInesistenteException {
		this(new Psp(codIntermediarioPsp, codPsp, ragioneSociale), codCanale, tipoVersamento);
	}
	
	public Canale(Psp psp, String codCanale, TipoVersamento tipoVersamento) {
		super(codCanale, tipoVersamento);
		this.psp = psp;
	}
	
	// Business
	
	private transient Psp psp;
	
	public Psp getPsp() {
		return this.psp;
	}
}
