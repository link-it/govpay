package it.govpay.ragioneria.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v2.beans.UnitaOperativa;

public class UnitaOperativaConverter {


	public static UnitaOperativa toRsModel(it.govpay.bd.model.UnitaOperativa uo) throws ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();

		rsModel.setAbilitato(uo.isAbilitato());
		rsModel.setArea(uo.getAnagrafica().getArea());
		rsModel.setCap(uo.getAnagrafica().getCap());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setEmail(uo.getAnagrafica().getEmail());
		rsModel.setFax(uo.getAnagrafica().getFax());
		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setNazione(uo.getAnagrafica().getNazione());
		rsModel.setPec(uo.getAnagrafica().getPec());
		rsModel.setProvincia(uo.getAnagrafica().getProvincia());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		rsModel.setTel(uo.getAnagrafica().getTelefono());
		rsModel.setWeb(uo.getAnagrafica().getUrlSitoWeb());
		return rsModel;
	}

}
