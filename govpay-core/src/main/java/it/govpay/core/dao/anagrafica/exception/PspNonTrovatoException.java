package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class PspNonTrovatoException extends RedirectException{

	public PspNonTrovatoException(String location) {
		super(location);
	}
	
	public PspNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public PspNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public PspNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
