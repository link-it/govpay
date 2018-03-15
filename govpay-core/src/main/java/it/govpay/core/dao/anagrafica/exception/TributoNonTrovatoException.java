package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class TributoNonTrovatoException extends NonTrovataException{

	public TributoNonTrovatoException(String message) {
		super(message);
	}
	public TributoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
