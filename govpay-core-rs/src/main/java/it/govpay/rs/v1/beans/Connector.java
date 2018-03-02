package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Connettore.EnumAuthType;

public class Connector extends it.govpay.rs.v1.beans.base.Connector {

	@Override
	public String getJsonIdFilter() {
		return "connector";
	}
	
	public static Connector parse(String json) {
		return (Connector) parse(json, Connector.class);
	}
	
	public Connector(it.govpay.model.Connettore connettore) throws ServiceException {
		if(!connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			this.setAuth(new it.govpay.rs.v1.beans.TipoAutenticazione(connettore));
		this.setUrl(connettore.getUrl());
		if(connettore.getVersione() != null)
			this.setVersioneApi(VersioneApiEnum.fromValue(connettore.getVersione().getApiLabel()));
	}
}
