package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class TransazioneRptException extends RedirectException{

	public TransazioneRptException(String location) {
		super(location);
	}
	
	public TransazioneRptException(String location, String message) {
		super(location,message);
	}
	
	public TransazioneRptException(String location,Throwable t) {
		super(location, t);
	}
	
	public TransazioneRptException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
