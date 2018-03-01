package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class ApplicazioneNonTrovataException extends RedirectException{

	public ApplicazioneNonTrovataException(String location) {
		super(location);
	}
	
	public ApplicazioneNonTrovataException(String location, String message) {
		super(location,message);
	}
	
	public ApplicazioneNonTrovataException(String location,Throwable t) {
		super(location, t);
	}
	
	public ApplicazioneNonTrovataException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
