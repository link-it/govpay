package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class OperatoreNonTrovatoException extends NonTrovataException{

	public OperatoreNonTrovatoException(String message) {
		super(message);
	}
	public OperatoreNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
