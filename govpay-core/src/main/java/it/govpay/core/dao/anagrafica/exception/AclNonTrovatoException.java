package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class AclNonTrovatoException extends RedirectException{

	public AclNonTrovatoException(String location) {
		super(location);
	}
	
	public AclNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public AclNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public AclNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
