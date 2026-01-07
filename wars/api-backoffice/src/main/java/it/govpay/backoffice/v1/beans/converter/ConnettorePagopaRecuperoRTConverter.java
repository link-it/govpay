/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import it.govpay.backoffice.v1.beans.ConnettorePagopaRecuperoRT;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Intermediario;

public class ConnettorePagopaRecuperoRTConverter {

	private ConnettorePagopaRecuperoRTConverter() {}

	public static Connettore getConnettore(it.govpay.backoffice.v1.beans.ConnettorePagopaRecuperoRT connector, String idIntermediario) {
		Connettore connettore = new Connettore();

		connettore.setIdConnettore(idIntermediario + Intermediario.CONNETTORE_RECUPERO_RT_SUFFIX);
		ConnettoriConverter.setAutenticazione(connettore, connector.getAuth());

		connettore.setUrl(connector.getUrl());
		connettore.setSubscriptionKeyValue(connector.getSubscriptionKey());
		connettore.setAbilitaGDE(connector.getAbilitaGDE());

		return connettore;
	}

	public static ConnettorePagopaRecuperoRT toRsModel(it.govpay.model.Connettore connettore) {
		ConnettorePagopaRecuperoRT rsModel = new ConnettorePagopaRecuperoRT();
		if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrl(connettore.getUrl());
		rsModel.subscriptionKey(connettore.getSubscriptionKeyValue());
		rsModel.setAbilitaGDE(connettore.isAbilitaGDE());

		return rsModel;
	}
}
