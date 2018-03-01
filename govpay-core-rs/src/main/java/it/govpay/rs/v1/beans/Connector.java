package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

public class Connector extends it.govpay.rs.v1.beans.base.Connector {

	@Override
	public String getJsonIdFilter() {
		return "connector";
	}
	
	public static Connector parse(String json) {
		return (Connector) parse(json, Connector.class);
	}
	
	public Connector(it.govpay.model.Connettore connettore) throws ServiceException {
		this.setAuth(connettore.getTipoAutenticazione());
		this.setUrl(connettore.getUrl());
//		this.setVersione(connettore.getVersione()); //TODO 1.0
//		this.setServizioNotifica(new Connector);
//		this.setIdCanale(canale.getCodCanale());
//		this.setModelloPagamento(ModelloPagamento.fromValue(canale.getModelloPagamento().toString()));
//		this.setPsp(UriBuilderUtils.getPsp(canale.getPsp(bd).getCodPsp()));
//		this.setTipoVersamento(TipoVersamento.fromValue(canale.getTipoVersamento().toString()));
	}
}
