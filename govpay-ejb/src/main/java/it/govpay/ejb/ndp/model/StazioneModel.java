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
package it.govpay.ejb.ndp.model;

import java.util.List;

import it.govpay.ejb.core.model.ScadenzarioModel;

public class StazioneModel {
	private String idIntermediarioPA;
	private String idStazioneIntermediarioPA;
	private String password;
	private List<ScadenzarioModel> scadenzari;
	
	public String getIdStazioneIntermediarioPA() {
		return idStazioneIntermediarioPA;
	}
	public void setIdStazioneIntermediarioPA(String idStazioneIntermediarioPA) {
		this.idStazioneIntermediarioPA = idStazioneIntermediarioPA;
	}
	public List<ScadenzarioModel> getScadenzari() {
		return scadenzari;
	}
	public void setScadenzari(List<ScadenzarioModel> scadenzari) {
		this.scadenzari = scadenzari;
	}
	public ScadenzarioModel getScadenzario(String idEnte){
		for(ScadenzarioModel scadenzarioModel : getScadenzari()) {
			if(scadenzarioModel.getIdEnte().equals(idEnte)) return scadenzarioModel;
		}
		return null;
	}
	public String getIdIntermediarioPA() {
		return idIntermediarioPA;
	}
	public void setIdIntermediarioPA(String idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StazioneModel) {
			return (this.idIntermediarioPA.equals(((StazioneModel) obj).getIdIntermediarioPA()) && this.idStazioneIntermediarioPA.equals(((StazioneModel) obj).getIdStazioneIntermediarioPA()));
		}
		return false;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
