package it.govpay.pendenze.v3.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

//import it.govpay.pendenze.v3.api.impl.DominiApiServiceImpl;
//import it.govpay.pendenze.v3.beans.Domini;
import it.govpay.pendenze.v3.beans.Dominio;
import it.govpay.pendenze.v3.beans.UnitaOperativa;

public class DominiConverter {
	
	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		Dominio rsModel = new Dominio();
		rsModel.setCap(dominio.getAnagrafica().getCap());
		rsModel.setCbill(dominio.getCbill());
		rsModel.setCivico(dominio.getAnagrafica().getCivico());
		rsModel.setEmail(dominio.getAnagrafica().getEmail());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setGln(dominio.getGln());
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
//		if(dominio.getLogo() != null) {
//			rsModel.setLogo(DominiApiServiceImpl.basePath.clone().path(dominio.getCodDominio()).path("logo").build().toString());
//		} 
		rsModel.setNazione(dominio.getAnagrafica().getNazione());
		rsModel.setPec(dominio.getAnagrafica().getPec());
		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		
		return rsModel;
	}
	
	
	public static UnitaOperativa toUnitaOperativaRsModel(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();
		rsModel.setArea(uo.getAnagrafica().getArea());
		rsModel.setCap(uo.getAnagrafica().getRagioneSociale());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setEmail(uo.getAnagrafica().getEmail());
		rsModel.setIdUnitaOperativa(uo.getCodUo());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setNazione(uo.getAnagrafica().getNazione());
		rsModel.setPec(uo.getAnagrafica().getPec());
		rsModel.setProvincia(uo.getAnagrafica().getProvincia());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		rsModel.setTel(uo.getAnagrafica().getTelefono());
		rsModel.setWeb(uo.getAnagrafica().getUrlSitoWeb());
		rsModel.setFax(uo.getAnagrafica().getFax());
		rsModel.setArea(uo.getAnagrafica().getArea());
		return rsModel;
	}


//	public static Domini toRsModel(List<it.govpay.bd.model.Dominio> results, int offset, int limit, long totalResults, UriInfo uriInfo) throws ServiceException {
//		Domini domini = new Domini();
//		domini.setItems(new ArrayList<Dominio>());
//		ConverterUtils.popolaLista(domini, uriInfo.getRequestUriBuilder(), results.size(), offset, limit, totalResults);
//		for(it.govpay.bd.model.Dominio d : results) {
//			domini.addItemsItem(toRsModel(d));
//		}
//		return domini;
//	}
}
