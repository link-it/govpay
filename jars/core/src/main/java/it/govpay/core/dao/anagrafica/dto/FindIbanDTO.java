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
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class FindIbanDTO extends BasicFindRequestDTO {
	
	private String codDominio;
	private String iban;
	private Boolean abilitato;
	
	public FindIbanDTO(Authentication user, String codDominio) {
		super(user);
		this.codDominio = codDominio;
		this.addSortField("ibanAccredito", it.govpay.orm.IbanAccredito.model().COD_IBAN);
	}
	
	public String getCodDominio(){
		return this.codDominio;
	}

	public Boolean getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
	
}
