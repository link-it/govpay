/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.console.anagrafica.model;


public class ScadenzarioModel extends it.govpay.ejb.model.ScadenzarioModel {
	
	public ScadenzarioModel(it.govpay.ejb.model.ScadenzarioModel basicModel) {
		this.setConnettoreNotifica(basicModel.getConnettoreNotifica());
		this.setConnettoreVerifica(basicModel.getConnettoreVerifica());
		this.setDescrizione(basicModel.getDescrizione());
		this.setIdEnte(basicModel.getIdEnte());
		this.setIdSystem(basicModel.getIdSystem());
		this.setIdStazione(basicModel.getIdStazione()); 
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	private String denominazioneEnte;
}
