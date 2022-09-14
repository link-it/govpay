package it.govpay.ragioneria.v2.beans.converter;

import it.govpay.ragioneria.v2.beans.Dominio;

public class DominiConverter {


	public static Dominio toRsModelIndex(it.govpay.bd.model.Dominio dominio) {
		Dominio rsModel = new Dominio();
//		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
//		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
//		rsModel.setCivico(dominio.getAnagrafica().getCivico());
//		rsModel.setCap(dominio.getAnagrafica().getCap());
//		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
//		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
//		rsModel.setNazione(dominio.getAnagrafica().getNazione());
//		rsModel.setEmail(dominio.getAnagrafica().getEmail());
//		rsModel.setPec(dominio.getAnagrafica().getPec());
//		rsModel.setTel(dominio.getAnagrafica().getTelefono());
//		rsModel.setFax(dominio.getAnagrafica().getFax());
//		rsModel.setGln(dominio.getGln());
//		rsModel.setAuxDigit("" + dominio.getAuxDigit());
//		if(dominio.getSegregationCode() != null)
//			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
//		if(dominio.getLogo() != null) {
//			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
//		}
//		rsModel.setIuvPrefix(dominio.getIuvPrefix());
//		rsModel.setStazione(dominio.getStazione().getCodStazione());
//		rsModel.setAbilitato(dominio.isAbilitato());

		return rsModel;
	}

}
