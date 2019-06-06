package it.govpay.core.dao.configurazione.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class ConfigurazioneNonTrovataException extends NonTrovataException{

	public ConfigurazioneNonTrovataException(String message) {
		super(message);
	}
	public ConfigurazioneNonTrovataException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
