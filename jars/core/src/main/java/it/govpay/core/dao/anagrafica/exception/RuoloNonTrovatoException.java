package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

public class RuoloNonTrovatoException extends NonTrovataException{

	public RuoloNonTrovatoException(String message) {
		super(message);
	}
	
	public RuoloNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
