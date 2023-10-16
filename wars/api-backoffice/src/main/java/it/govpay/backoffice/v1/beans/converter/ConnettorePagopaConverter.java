package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.ConnettorePagopa;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;

public class ConnettorePagopaConverter {

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
