package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class IntermediarioNonTrovatoException extends NonTrovataException{

	public IntermediarioNonTrovatoException(String message) {
		super(message);
	}
	public IntermediarioNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
