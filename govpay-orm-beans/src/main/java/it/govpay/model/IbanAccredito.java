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

public class IbanAccredito extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id; 
	private String codIban;
	private String codBic;
	private String idSellerBank;
	private String idNegozio;
	private boolean postale;
	private boolean attivatoObep;
	private boolean abilitato;
	private Long idDominio;
	
	public Long getId() {
		return id;
	}
	public String getCodIban() {
		return codIban;
	}
	public void setCodIban(String codIban) {
		this.codIban = codIban;
	}
	public String getIdSellerBank() {
		return idSellerBank;
	}
	public void setIdSellerBank(String idSellerBank) {
		this.idSellerBank = idSellerBank;
	}
	public String getIdNegozio() {
		return idNegozio;
	}
	public void setIdNegozio(String idNegozio) {
		this.idNegozio = idNegozio;
	}
	public boolean isPostale() {
		return postale;
	}
	public void setPostale(boolean postale) {
		this.postale = postale;
	}
	public boolean isAttivatoObep() {
		return attivatoObep;
	}
	public void setAttivatoObep(boolean attivatoObep) {
		this.attivatoObep = attivatoObep;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodBic() {
		return this.codBic;
	}
	public void setCodBic(String codBic) {
		this.codBic = codBic;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
}

