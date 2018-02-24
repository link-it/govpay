package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class DominioNonTrovatoException extends RedirectException{

	public DominioNonTrovatoException(String location) {
		super(location);
	}
	
	public DominioNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public DominioNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public DominioNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
