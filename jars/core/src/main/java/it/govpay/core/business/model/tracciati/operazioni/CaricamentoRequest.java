/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.model.tracciati.operazioni;

import it.govpay.model.Operazione.TipoOperazioneType;


public class CaricamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;
	private it.govpay.core.dao.commons.Versamento versamento;
	private String tipoTemplateTrasformazioneRichiesta;
	private String templateTrasformazioneRichiesta;
	private Boolean avvisaturaAbilitata;
	private String avvisaturaModalita;

	public CaricamentoRequest(){
		super(TipoOperazioneType.ADD);
	}

	public String getMotivoAnnullamento() {
		return this.motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}

	public it.govpay.core.dao.commons.Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(it.govpay.core.dao.commons.Versamento versamento) {
		this.versamento = versamento;
	}

	public String getTemplateTrasformazioneRichiesta() {
		return templateTrasformazioneRichiesta;
	}

	public void setTemplateTrasformazioneRichiesta(String templateTrasformazioneRichiesta) {
		this.templateTrasformazioneRichiesta = templateTrasformazioneRichiesta;
	}
	
	public Boolean getAvvisaturaAbilitata() {
		return avvisaturaAbilitata;
	}
	public void setAvvisaturaAbilitata(Boolean avvisaturaAbilitata) {
		this.avvisaturaAbilitata = avvisaturaAbilitata;
	}
	public String getAvvisaturaModalita() {
		return avvisaturaModalita;
	}
	public void setAvvisaturaModalita(String avvisaturaModalita) {
		this.avvisaturaModalita = avvisaturaModalita;
	}

	public String getTipoTemplateTrasformazioneRichiesta() {
		return tipoTemplateTrasformazioneRichiesta;
	}

	public void setTipoTemplateTrasformazioneRichiesta(String tipoTemplateTrasformazioneRichiesta) {
		this.tipoTemplateTrasformazioneRichiesta = tipoTemplateTrasformazioneRichiesta;
	}
	 
}
