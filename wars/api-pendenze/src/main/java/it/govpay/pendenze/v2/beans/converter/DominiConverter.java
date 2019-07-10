package it.govpay.pendenze.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.pendenze.v2.beans.Dominio;

public class DominiConverter {
	
	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		Dominio rsModel = new Dominio();
		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		rsModel.setCivico(dominio.getAnagrafica().getCivico());
		rsModel.setCap(dominio.getAnagrafica().getCap());
		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
		rsModel.setNazione(dominio.getAnagrafica().getNazione());
		rsModel.setEmail(dominio.getAnagrafica().getEmail());
		rsModel.setPec(dominio.getAnagrafica().getPec());
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setGln(dominio.getGln());
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		
		return rsModel;
	}
}
