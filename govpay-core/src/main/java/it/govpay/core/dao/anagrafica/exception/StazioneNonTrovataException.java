package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class StazioneNonTrovataException extends RedirectException{

	public StazioneNonTrovataException(String location) {
		super(location);
	}
	
	public StazioneNonTrovataException(String location, String message) {
		super(location,message);
	}
	
	public StazioneNonTrovataException(String location,Throwable t) {
		super(location, t);
	}
	
	public StazioneNonTrovataException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
