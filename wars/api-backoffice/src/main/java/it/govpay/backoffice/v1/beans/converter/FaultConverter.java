package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.FaultBean;
import it.govpay.backoffice.v1.controllers.BaseController;
import it.govpay.core.exceptions.IOException;

public class FaultConverter {

	protected String getRespJson(FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson = respKo.toJSON(null);
		} catch(IOException ex) {
			respKoJson = BaseController.ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
	}
}
