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

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.TipoVersamentoDominio;

public class PutTipoPendenzaDominioDTO extends BasicCreateRequestDTO  {
	
	private String idDominio;
	private TipoVersamentoDominio tipoVersamentoDominio;
	private String codTipoVersamento;
	
	public PutTipoPendenzaDominioDTO(Authentication user) {
		super(user);
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	public TipoVersamentoDominio getTipoVersamentoDominio() {
		return tipoVersamentoDominio;
	}

	public void setTipoVersamentoDominio(TipoVersamentoDominio tipoVersamentoDominio) {
		this.tipoVersamentoDominio = tipoVersamentoDominio;
	}


}
