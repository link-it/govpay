package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class TributoNonTrovatoException extends RedirectException{

	public TributoNonTrovatoException(String location) {
		super(location);
	}
	
	public TributoNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public TributoNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public TributoNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
