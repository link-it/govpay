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
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.commons.Dominio;

public class BasicRequestDTO {
	
	private List<IdUnitaOperativa> unitaOperative;
	private List<Dominio> domini;
	private List<Long> idDomini;
	private List<String> codDomini;
	private List<Long> idTipiVersamento;
	private List<String> codTipiVersamento;
	private Authentication authentication;
 	
	public BasicRequestDTO(Authentication authentication) {
		this.authentication = authentication;
		this.idDomini = null;
		this.codDomini = null;
		this.idTipiVersamento = null;
		this.codTipiVersamento = null;
		this.domini = null;
		this.unitaOperative = null;
	}
	
	public Authentication getUser() {
		return this.authentication;
	}
	
	public Long getIdOperatore() {
		return AutorizzazioneUtils.getIdOperatore(this.authentication);
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public List<String> getCodTipiVersamento() {
		return codTipiVersamento;
	}

	public void setCodTipiVersamento(List<String> codTipiVersamento) {
		this.codTipiVersamento = codTipiVersamento;
	}

	public List<Dominio> getDomini() {
		return domini;
	}

	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}

	public List<IdUnitaOperativa> getUnitaOperative() {
		return unitaOperative;
	}

	public void setUnitaOperative(List<IdUnitaOperativa> unitaOperative) {
		this.unitaOperative = unitaOperative;
	}
}
