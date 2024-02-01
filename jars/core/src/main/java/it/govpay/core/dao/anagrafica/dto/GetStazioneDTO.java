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

public class GetStazioneDTO extends BasicRequestDTO {
	
	public GetStazioneDTO(Authentication user, String codIntermediario, String codStazione) {
		super(user);
		this.codIntermediario = codIntermediario;
		this.codStazione = codStazione;
	}

	private String codStazione;
	
	public String getCodStazione() {
		return this.codStazione;
	}
	
	private String codIntermediario;
	
	public String getCodIntermediario() {
		return this.codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	
}
