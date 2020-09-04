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
	private boolean postale;
	private boolean abilitato;
	private Long idDominio;
	private String descrizione;
	private String intestatario;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public String getCodIban() {
		return this.codIban;
	}
	public void setCodIban(String codIban) {
		this.codIban = codIban;
	}
	public boolean isPostale() {
		return this.postale;
	}
	public void setPostale(boolean postale) {
		this.postale = postale;
	}
	public boolean isAbilitato() {
		return this.abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public String getCodBic() {
		return this.codBic;
	}
	public void setCodBic(String codBic) {
		this.codBic = codBic;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getIntestatario() {
		return intestatario;
	}
	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}
}

