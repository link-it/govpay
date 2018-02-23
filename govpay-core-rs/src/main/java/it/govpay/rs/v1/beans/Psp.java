package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

public class Psp extends it.govpay.rs.v1.beans.base.Psp {

	@Override
	public String getJsonIdFilter() {
		return "psp";
	}
	
	public static Psp parse(String json) {
		return (Psp) parse(json, Psp.class);
	}
	
	public Psp(it.govpay.bd.model.Psp psp) throws ServiceException {
		
	}
}
