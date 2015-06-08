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
package it.govpay.ejb.core.builder;

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.core.model.TributoModel.EnumStatoTributo;
import it.govpay.orm.profilazione.Ente;
import it.govpay.orm.profilazione.IndirizzoPostale;
import it.govpay.orm.profilazione.TributoEnte;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreditoreBuilder {

	public static EnteCreditoreModel fromEnte(Ente ente) {

		if (ente != null) {
			EnteCreditoreModel creditoreModel = new EnteCreditoreModel();
			IndirizzoPostale indirizzo = ente.getIntestatario().getIndirizzipostali();
			
			creditoreModel.setIdFiscale(ente.getIntestatario().getLapl());
			creditoreModel.setIndirizzo(indirizzo.getAddress());
			creditoreModel.setCivico(indirizzo.getNumeroCivico());
			creditoreModel.setLocalita(indirizzo.getCity());
			creditoreModel.setCap(indirizzo.getCapCode());
			creditoreModel.setIdentificativoUnivoco(ente.getCodiceEnte());
			creditoreModel.setDenominazione(ente.getDenominazione());
			creditoreModel.setIdEnteCreditore(ente.getIdEnte());
			creditoreModel.setProvincia(ente.getProvincia() != null ? ente.getProvincia() : indirizzo.getProvince());
			creditoreModel.setStato(EnteCreditoreModel.EnumStato.valueOf(ente.getStato()));
			creditoreModel.setNumFax(indirizzo.getFaxNumber());
			creditoreModel.setNumTelefono(indirizzo.getPhoneNumber());
			Set<TributoEnte> tributi = ente.getTributiEnte();
			if (tributi != null) {
				List<TributoModel> tributiGestiti = new ArrayList<TributoModel>();
				for (TributoEnte tributo : tributi) {
					TributoModel tributoModel = new TributoModel();

					tributoModel.setCodiceTributo(tributo.getCdTrbEnte());
					tributoModel.setIdSistema(tributo.getSIL());
					tributoModel.setIdTributo(tributo.getIdTributo());
					tributoModel.setStato(EnumStatoTributo.valueOf(tributo.getStato()));
					tributoModel.setDescrizione(tributo.getDeTrb());
					tributoModel.setIbanAccredito(tributo.getIBAN()); 

					tributiGestiti.add(tributoModel);
				}
				creditoreModel.setTributiGestiti(tributiGestiti);
			}
			
			return creditoreModel;

		} else {
			return null;
		}
	}

}
