/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;

public class GetDominioDTOResponse {
	
	private Dominio dominio;
	private List<UnitaOperativa> uo;
	private List<IbanAccredito> iban;
	private List<Tributo> tributi;
	private List<TipoVersamentoDominio> tipiVersamentoDominio;
	
	public GetDominioDTOResponse(Dominio dominio) {
		this.dominio = dominio;
	}

	public Dominio getDominio() {
		return this.dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public List<IbanAccredito> getIban() {
		return this.iban;
	}

	public void setIban(List<IbanAccredito> iban) {
		this.iban = iban;
	}

	public List<Tributo> getTributi() {
		return this.tributi;
	}

	public void setTributi(List<Tributo> tributi) {
		this.tributi = tributi;
	}

	public List<UnitaOperativa> getUo() {
		return this.uo;
	}

	public void setUo(List<UnitaOperativa> uo) {
		this.uo = uo;
	}

	public List<TipoVersamentoDominio> getTipiVersamentoDominio() {
		return tipiVersamentoDominio;
	}

	public void setTipiVersamentoDominio(List<TipoVersamentoDominio> tipiVersamentoDominio) {
		this.tipiVersamentoDominio = tipiVersamentoDominio;
	}

}
