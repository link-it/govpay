package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class IntermediarioNonTrovatoException extends RedirectException{

	public IntermediarioNonTrovatoException(String location) {
		super(location);
	}
	
	public IntermediarioNonTrovatoException(String location, String message) {
		super(location,message);
	}
	
	public IntermediarioNonTrovatoException(String location,Throwable t) {
		super(location, t);
	}
	
	public IntermediarioNonTrovatoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
