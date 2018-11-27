package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.FaultBean;
import it.govpay.backoffice.v1.controllers.BaseController;

public class FaultConverter {

	protected String getRespJson(FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson = respKo.toJSON(null);
		} catch(ServiceException ex) {
			respKoJson = BaseController.ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
	}
}
