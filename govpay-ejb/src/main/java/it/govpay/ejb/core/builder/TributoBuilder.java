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
package it.govpay.ejb.builder;

import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.model.TributoModel.EnumStatoTributo;
import it.govpay.orm.profilazione.TributoEnte;

public class TributoBuilder {

	public static TributoModel fromTributoEnte(TributoEnte tributoEnte) {
		
		if(tributoEnte == null) {
			return null;
		}

		TributoModel tributo = new TributoModel();
		tributo.setCodiceTributo(tributoEnte.getCdTrbEnte());
		tributo.setIdTributo(tributoEnte.getIdTributo());
		tributo.setIdSistema(tributoEnte.getSistemaEnte().getSisEntId().getIdSystem());
		tributo.setIdEnteCreditore(tributoEnte.getSistemaEnte().getSisEntId().getIdEnte());
		tributo.setDescrizione(tributoEnte.getDeTrb());
		tributo.setStato(EnumStatoTributo.valueOf(tributoEnte.getStato()));

		return tributo;

	}

}
