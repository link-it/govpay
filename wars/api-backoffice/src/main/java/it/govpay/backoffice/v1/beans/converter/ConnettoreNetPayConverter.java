package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ConnettoreNetPay.VersioneApiEnum;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Versionabile;
import it.govpay.model.ConnettoreNotificaPagamenti.Tipo;

public class ConnettoreNetPayConverter {
	
	public static it.govpay.model.ConnettoreNotificaPagamenti getConnettoreDTO(it.govpay.backoffice.v1.beans.ConnettoreNetPay connector, Authentication user, Tipo tipo) throws ServiceException,NotAuthorizedException {
		it.govpay.model.ConnettoreNotificaPagamenti connettore = new it.govpay.model.ConnettoreNotificaPagamenti();
		
		connettore.setAbilitato(connector.Abilitato());
		
		if(connector.Abilitato()) {
			connettore.setTipoTracciato(tipo.name());
			connettore.setNetPayPrincipal(connector.getPrincipal());
			connettore.setNetPayURL(connector.getUrl());
			if(connector.getVersioneApi() != null)
				connettore.setVersione(Versionabile.Versione.toEnum(VersioneApiEnum.fromValue(connector.getVersioneApi()).toNameString()));
			connettore.setNetPayCompany(connector.getCompany());
			connettore.setNetPayPassword(connector.getPassword());
			connettore.setNetPayRuolo(connector.getRuolo());
			connettore.setNetPayUsername(connector.getUsername());
		}
		
		return connettore;
	}

	public static it.govpay.backoffice.v1.beans.ConnettoreNetPay toRsModel(it.govpay.model.ConnettoreNotificaPagamenti connettore) throws ServiceException {
		it.govpay.backoffice.v1.beans.ConnettoreNetPay rsModel = new it.govpay.backoffice.v1.beans.ConnettoreNetPay();
		
		rsModel.setAbilitato(connettore.isAbilitato());
		if(connettore.isAbilitato()) {
			rsModel.setPrincipal(connettore.getNetPayPrincipal());
			rsModel.setUrl(connettore.getNetPayURL());
			if(connettore.getVersione() != null)
				rsModel.setVersioneApi(VersioneApiEnum.fromName(connettore.getVersione().getApiLabel()).toString());
			rsModel.setCompany(connettore.getNetPayCompany());
			rsModel.setPassword(connettore.getNetPayPassword());
			rsModel.setRuolo(connettore.getNetPayRuolo());
			rsModel.setUsername(connettore.getNetPayUsername());
		}
		return rsModel;
	}
}
