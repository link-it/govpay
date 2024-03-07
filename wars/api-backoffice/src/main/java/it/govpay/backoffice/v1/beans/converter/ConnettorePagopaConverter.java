/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.ConnettorePagopa;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;

public class ConnettorePagopaConverter {
	
	private ConnettorePagopaConverter() {}

	public static Connettore getConnettore(it.govpay.backoffice.v1.beans.ConnettorePagopa connector) {
		Connettore connettore = new Connettore();

		ConnettoriConverter.setAutenticazione(connettore, connector.getAuth());

		connettore.setUrl(connector.getUrlRPT());
		connettore.setUrlServiziAvvisatura(connector.getUrlAvvisatura());
		connettore.setSubscriptionKeyValue(connector.getSubscriptionKey());

		return connettore;
	}

	public static ConnettorePagopa toRsModel(it.govpay.model.Connettore connettore) {
		ConnettorePagopa rsModel = new ConnettorePagopa();
		if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrlRPT(connettore.getUrl());
		rsModel.setUrlAvvisatura(connettore.getUrlServiziAvvisatura());
		rsModel.subscriptionKey(connettore.getSubscriptionKeyValue());

		return rsModel;
	}
}
