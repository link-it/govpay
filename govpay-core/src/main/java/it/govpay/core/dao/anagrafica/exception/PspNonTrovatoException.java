package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class PspNonTrovatoException extends NonTrovataException{

	public PspNonTrovatoException(String message) {
		super(message);
	}

	public PspNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
