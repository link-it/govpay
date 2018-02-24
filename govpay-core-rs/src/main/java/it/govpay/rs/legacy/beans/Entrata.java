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
package it.govpay.rs.legacy.beans;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.rs.v1.beans.JSONSerializable;

@JsonFilter(value="entrate")  
public class Entrata extends JSONSerializable {
	private String href;
	private String idEntrata;
	private String descrizione;
	private String ibanAccredito;
	private String tipoContabilita;
	private String codContabilita;
	private boolean abilitato;
	
	public Entrata() {

	}

	public Entrata(it.govpay.bd.model.Tributo tributo, String codDominio, UriBuilder uriBuilder) throws ServiceException {
		uriBuilder.path("domini").path(codDominio).path("entrate").path(tributo.getCodTributo());
		this.abilitato = tributo.isAbilitato();
		this.descrizione = tributo.getDescrizione();
		this.idEntrata = tributo.getCodTributo();
		this.href = uriBuilder.build().toString();
		this.abilitato = tributo.isAbilitato();
		this.descrizione = tributo.getDescrizione();
		this.ibanAccredito = tributo.getIbanAccredito().getCodIban();
		this.tipoContabilita = tributo.getTipoContabilita().name();
		this.codContabilita = tributo.getCodContabilita();
	}
	
	@Override
	public String getJsonIdFilter() {
		return "entrate";
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIdEntrata() {
		return idEntrata;
	}

	public void setIdEntrata(String idEntrata) {
		this.idEntrata = idEntrata;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public String getTipoContabilita() {
		return tipoContabilita;
	}

	public void setTipoContabilita(String tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	public String getCodContabilita() {
		return codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
}

