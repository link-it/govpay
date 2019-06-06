package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class UnitaOperativaNonTrovataException extends NonTrovataException{
	public UnitaOperativaNonTrovataException(String message) {
		super(message);
	}
	public UnitaOperativaNonTrovataException(String message, Throwable t) {
		super(message, t);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
