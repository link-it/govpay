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

import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.orm.gde.GdeInfoSpCoop;

public class GdeInfoSpCoopBuilder {
	
	public static GdeInfoSpCoop toGdeInfoSpCoop(Infospcoop infospcoop) {
		
		if(infospcoop == null) {
			return null;
		} else {
		
			GdeInfoSpCoop gdeInfoSpCoop = new GdeInfoSpCoop();
	
			gdeInfoSpCoop.setAzione(infospcoop.getAzione());
			gdeInfoSpCoop.setIdEgov(infospcoop.getIdEgov());
			gdeInfoSpCoop.setServizio(infospcoop.getServizio());
			gdeInfoSpCoop.setSoggettoErogatore(infospcoop.getSoggettoErogatore());
			gdeInfoSpCoop.setSoggettoFruitore(infospcoop.getSoggettoFruitore());
			gdeInfoSpCoop.setTipoServizio(infospcoop.getTipoServizio());
			gdeInfoSpCoop.setTipoSoggettoErogatore(infospcoop.getTipoSoggettoErogatore());
			gdeInfoSpCoop.setTipoSoggettoFruitore(infospcoop.getTipoSoggettoFruitore());
	
			return gdeInfoSpCoop;
		}
	}

	public static Infospcoop fromGdeInfoSpCoop(GdeInfoSpCoop gdeInfoSpCoop) {
		
		if(gdeInfoSpCoop == null) {
			return null;
		} else {

			Infospcoop infospcoop = new EventiInterfacciaModel().new Infospcoop();

			infospcoop.setAzione(gdeInfoSpCoop.getAzione());
			infospcoop.setIdEgov(gdeInfoSpCoop.getIdEgov());
			infospcoop.setServizio(gdeInfoSpCoop.getServizio());
			infospcoop.setSoggettoErogatore(gdeInfoSpCoop.getSoggettoErogatore());
			infospcoop.setSoggettoFruitore(gdeInfoSpCoop.getSoggettoFruitore());
			infospcoop.setTipoServizio(gdeInfoSpCoop.getTipoServizio());
			infospcoop.setTipoSoggettoErogatore(gdeInfoSpCoop.getTipoSoggettoErogatore());
			infospcoop.setTipoSoggettoFruitore(gdeInfoSpCoop.getTipoSoggettoFruitore());
			
			return infospcoop;
		}

	}
}
