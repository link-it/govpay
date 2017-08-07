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
package it.govpay.web.rs.v1.beans;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

@JsonFilter(value="unitaOperative")  
public class UnitaOperativa extends JSONSerializable {
	private String href;
	private String codiceUnivoco;
	private String ragioneSociale;
	private String indirizzo;
	private String civico;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private boolean abilitato;
	
	
	public UnitaOperativa() {
	}

	public UnitaOperativa(it.govpay.bd.model.UnitaOperativa uo, String codDominio, UriBuilder uriBuilder) throws IllegalArgumentException, ServiceException {
		uriBuilder.path("domini").path(codDominio).path("unita_operative").path(uo.getAnagrafica().getCodUnivoco());
		this.ragioneSociale = uo.getAnagrafica().getRagioneSociale();
		this.abilitato = uo.isAbilitato();
		this.codiceUnivoco = uo.getAnagrafica().getCodUnivoco();
		this.href = uriBuilder.build().toString();
		this.indirizzo = uo.getAnagrafica().getIndirizzo();
		this.civico = uo.getAnagrafica().getCivico();
		this.cap = uo.getAnagrafica().getCap();
		this.localita = uo.getAnagrafica().getLocalita();
		this.provincia = uo.getAnagrafica().getProvincia();
		this.nazione = uo.getAnagrafica().getNazione();
	}
	
	@Override
	public String getJsonIdFilter() {
		return "unitaOperative";
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getCodiceUnivoco() {
		return codiceUnivoco;
	}

	public void setCodiceUnivoco(String codiceUnivoco) {
		this.codiceUnivoco = codiceUnivoco;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

}