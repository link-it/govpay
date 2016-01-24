/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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


/**
 * Rapppresenta un ente creditore
 */
public class IbanAccredito extends BasicModel implements Comparable<IbanAccredito>{
	private static final long serialVersionUID = 1L;

	private Long id; 
	private String codIban;
	private String codBicAccredito;
	private String codIbanAppoggio;
	private String codBicAppoggio;
	private String idSellerBank;
	private String idNegozio;
	private boolean postale;
	private boolean attivatoObep;
	private boolean abilitato;
	private Long idDominio;
	
	public IbanAccredito() {}	
	
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
	@Override
	public boolean equals(Object obj) {
		IbanAccredito iban = null;
		if(obj instanceof IbanAccredito) {
			iban = (IbanAccredito) obj;
		} else {
			return false;
		}

		boolean equal =
				equals(codIban, iban.getCodIban()) &&
				equals(codBicAccredito, iban.getCodBicAccredito()) &&
				equals(codIbanAppoggio, iban.getCodIbanAppoggio()) &&
				equals(codBicAppoggio, iban.getCodBicAppoggio()) &&
				equals(idSellerBank, iban.getIdSellerBank()) &&
				equals(idNegozio, iban.getIdNegozio()) &&
				equals(postale, iban.isPostale()) &&
				equals(attivatoObep, iban.isAttivatoObep()) &&
				equals(abilitato, iban.isAbilitato()) && 
				equals(idDominio, iban.getIdDominio());

		return equal;
	}
	public String getCodBicAccredito() {
		return codBicAccredito;
	}
	public void setCodBicAccredito(String codBicAccredito) {
		this.codBicAccredito = codBicAccredito;
	}
	public String getCodIbanAppoggio() {
		return codIbanAppoggio;
	}
	public void setCodIbanAppoggio(String codIbanAppoggio) {
		this.codIbanAppoggio = codIbanAppoggio;
	}
	public String getCodBicAppoggio() {
		return codBicAppoggio;
	}
	public void setCodBicAppoggio(String codBicAppoggio) {
		this.codBicAppoggio = codBicAppoggio;
	}

	public Long getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}

	@Override
	public int compareTo(IbanAccredito o) {
		return this.codIban.compareTo(o.getCodIban()); 
	}

}

