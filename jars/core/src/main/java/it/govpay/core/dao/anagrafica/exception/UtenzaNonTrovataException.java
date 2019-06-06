package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class UtenzaNonTrovataException extends NonTrovataException{

	public UtenzaNonTrovataException(String message) {
		super(message);
	}
	public UtenzaNonTrovataException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
