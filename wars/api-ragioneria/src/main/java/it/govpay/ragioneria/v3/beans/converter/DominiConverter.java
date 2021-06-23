package it.govpay.ragioneria.v3.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v3.beans.Dominio;

public class DominiConverter {
	
	
	public static Dominio toRsModelIndex(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		Dominio rsModel = new Dominio();
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		return rsModel;
	}
	
}
