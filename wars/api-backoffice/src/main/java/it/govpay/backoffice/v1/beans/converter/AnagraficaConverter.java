package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.Soggetto;
import it.govpay.model.Anagrafica;

public class AnagraficaConverter {


	public static Soggetto toSoggettoRsModel(it.govpay.model.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Soggetto rsModel = new Soggetto();

		if(anagrafica.getTipo() != null)
			rsModel.setTipo(Soggetto.TipoEnum.fromValue(anagrafica.getTipo().toString()));

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

	/**
	 * @param soggettoVersante
	 * @return
	 */
	public static Anagrafica toAnagrafica(Soggetto anagrafica) {
		if(anagrafica == null) return null;
		Anagrafica rsModel = new Anagrafica();

		if(anagrafica.getTipo() != null)
			rsModel.setTipo(Anagrafica.TIPO.valueOf(anagrafica.getTipo().toString()));

		rsModel.setCodUnivoco(anagrafica.getIdentificativo());
		rsModel.setRagioneSociale(anagrafica.getAnagrafica());
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
