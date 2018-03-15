package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

public class AclNonTrovatoException extends NonTrovataException{

	public AclNonTrovatoException(String message) {
		super(message);
	}
	
	public AclNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
