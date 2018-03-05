package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class UtenzaNonTrovataException extends RedirectException{

	public UtenzaNonTrovataException(String location) {
		super(location);
	}
	
	public UtenzaNonTrovataException(String location, String message) {
		super(location,message);
	}
	
	public UtenzaNonTrovataException(String location,Throwable t) {
		super(location, t);
	}
	
	public UtenzaNonTrovataException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
