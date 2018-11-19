package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

public class RicevutaNonTrovataException extends NonTrovataException{

	public RicevutaNonTrovataException(String message) {
		super(message);
	}
	public RicevutaNonTrovataException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
