package it.govpay.rs.v1.beans.pendenze.converter;

import it.govpay.core.rs.v1.beans.pendenze.Soggetto;
import it.govpay.core.rs.v1.beans.pendenze.Soggetto.TipoEnum;

public class AnagraficaConverter {

	
	public static Soggetto toSoggettoRsModel(it.govpay.model.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Soggetto rsModel = new Soggetto();

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
