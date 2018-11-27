package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

public class TracciatoNonTrovatoException extends NonTrovataException{

	public TracciatoNonTrovatoException(String message) {
		super(message);
	}
	public TracciatoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
