package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class OperatoreNonTrovatoException extends RedirectException{

	public OperatoreNonTrovatoException(String location) {
		super(location);
	}
	
	public OperatoreNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public OperatoreNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public OperatoreNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
