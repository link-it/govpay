/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Tributo;
import it.govpay.model.IbanAccredito;

public class GetTributoDTOResponse {
	
	private Tributo tributo;
	private IbanAccredito ibanAccredito;
	private IbanAccredito ibanAppoggio;
	
	public GetTributoDTOResponse(Tributo tributo, IbanAccredito ibanAccredito, IbanAccredito ibanAppoggio) {
		this.tributo = tributo;
		this.ibanAccredito = ibanAccredito;
		this.ibanAppoggio = ibanAppoggio;
	}

	public Tributo getTributo() {
		return this.tributo;
	}

	public IbanAccredito getIbanAccredito() {
		return this.ibanAccredito;
	}

	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public IbanAccredito getIbanAppoggio() {
		return this.ibanAppoggio;
	}

	public void setIbanAppoggio(IbanAccredito ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
	}

}
