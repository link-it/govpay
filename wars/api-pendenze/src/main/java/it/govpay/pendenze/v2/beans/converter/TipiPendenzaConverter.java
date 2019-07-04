package it.govpay.pendenze.v2.beans.converter;

import it.govpay.pendenze.v2.beans.TipoPendenza;

public class TipiPendenzaConverter {

	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
		return rsModel;
	}
}
