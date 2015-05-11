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
package it.govpay.ndp.util.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import it.govpay.ejb.builder.ScadenzarioBuilder;
import it.govpay.ndp.model.StazioneModel;
import it.govpay.orm.profilazione.StazioneIntermediario;

public class StazioneBuilder {

	public static StazioneModel fromStazioneIntermediario(StazioneIntermediario stazione, EntityManager entityManager) {
		if (stazione == null) {
			return null;
		}
		StazioneModel stazioneModel = new StazioneModel();
		stazioneModel.setIdIntermediarioPA(stazione.getIntermediario().getIdIntermediario());
		stazioneModel.setIdStazioneIntermediarioPA(stazione.getIdStazione());
		stazioneModel.setPassword(stazione.getPassword());
		stazioneModel.setScadenzari(ScadenzarioBuilder.fromSistemaEnte(stazione.getSistemiEnte(), entityManager));
		return stazioneModel;
	}
	
	public static List<StazioneModel> fromStazioneIntermediario(Set<StazioneIntermediario> stazioni, EntityManager entityManager) {
		if (stazioni == null) {
			return null;
		}
		
		List<StazioneModel> stazioniModel = new ArrayList<StazioneModel>();
		
		for(StazioneIntermediario stazione : stazioni) {
			stazioniModel.add(fromStazioneIntermediario(stazione, entityManager));
		}
		return stazioniModel;
	}
}
