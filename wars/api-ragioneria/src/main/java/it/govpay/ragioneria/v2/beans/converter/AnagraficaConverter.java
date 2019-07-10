package it.govpay.ragioneria.v2.beans.converter;

import it.govpay.ragioneria.v2.beans.Soggetto;
import it.govpay.ragioneria.v2.beans.Soggetto.TipoEnum;

public class AnagraficaConverter {

	
	public static Soggetto toSoggettoRsModel(it.govpay.model.Anagrafica anagrafica) {
		Soggetto rsModel = new Soggetto();

		if(anagrafica.getTipo() != null)
			rsModel.setTipo(TipoEnum.fromValue(anagrafica.getTipo().toString()));

		rsModel.setIdentificativo(anagrafica.getCodUnivoco());
		rsModel.setAnagrafica(anagrafica.getRagioneSociale());
		rsModel.setIndirizzo(anagrafica.getIndirizzo());
		rsModel.setCivico(anagrafica.getCivico());
		rsModel.setCap(anagrafica.getCap());
		rsModel.setLocalita(anagrafica.getLocalita());
		rsModel.setProvincia(anagrafica.getProvincia());
		rsModel.setNazione(anagrafica.getNazione());
		rsModel.setEmail(anagrafica.getEmail());
		rsModel.setCellulare(anagrafica.getCellulare());
		
		return rsModel;
	}
}
