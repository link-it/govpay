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

package it.govpay.core.business.model;

import java.util.List;

import it.govpay.bd.model.Canale;
import it.govpay.model.Portale;

public class AvviaTransazioneDTO {

	private Portale portale;
	private List<Object> versamentoOrVersamentoRef;
	private it.govpay.servizi.commons.Anagrafica versante;
	private String ibanAddebito;
	private String autenticazione;
	private String urlRitorno;
	private Boolean aggiornaSeEsisteB;
	private Canale canale;
	private String codiceConvenzione;
	
	public Portale getPortale() {
		return portale;
	}
	public void setPortale(Portale portale) {
		this.portale = portale;
	}
	public List<Object> getVersamentoOrVersamentoRef() {
		return versamentoOrVersamentoRef;
	}
	public void setVersamentoOrVersamentoRef(List<Object> versamentoOrVersamentoRef) {
		this.versamentoOrVersamentoRef = versamentoOrVersamentoRef;
	}
	public it.govpay.servizi.commons.Anagrafica getVersante() {
		return versante;
	}
	public void setVersante(it.govpay.servizi.commons.Anagrafica versante) {
		this.versante = versante;
	}
	public String getIbanAddebito() {
		return ibanAddebito;
	}
	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}
	public String getAutenticazione() {
		return autenticazione;
	}
	public void setAutenticazione(String autenticazione) {
		this.autenticazione = autenticazione;
	}
	public String getUrlRitorno() {
		return urlRitorno;
	}
	public void setUrlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
	}
	public Boolean getAggiornaSeEsisteB() {
		return aggiornaSeEsisteB;
	}
	public void setAggiornaSeEsisteB(Boolean aggiornaSeEsisteB) {
		this.aggiornaSeEsisteB = aggiornaSeEsisteB;
	}
	public Canale getCanale() {
		return canale;
	}
	public void setCanale(Canale canale) {
		this.canale = canale;
	}
	public String getCodiceConvenzione() {
		return codiceConvenzione;
	}
	public void setCodiceConvenzione(String codiceConvenzione) {
		this.codiceConvenzione = codiceConvenzione;
	}

}
