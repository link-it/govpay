package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class ApplicazioneNonTrovataException extends NonTrovataException{

	public ApplicazioneNonTrovataException(String message) {
		super(message);
	}
	public ApplicazioneNonTrovataException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
