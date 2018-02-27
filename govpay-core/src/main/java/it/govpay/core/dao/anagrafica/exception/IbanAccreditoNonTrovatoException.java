package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class IbanAccreditoNonTrovatoException extends RedirectException{

	public IbanAccreditoNonTrovatoException(String location) {
		super(location);
	}
	
	public IbanAccreditoNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public IbanAccreditoNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public IbanAccreditoNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
