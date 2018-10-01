package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class StazioneNonTrovataException extends NonTrovataException{

	public StazioneNonTrovataException(String message) {
		super(message);
	}
	public StazioneNonTrovataException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
