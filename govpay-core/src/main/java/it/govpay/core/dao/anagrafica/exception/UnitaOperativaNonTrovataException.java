package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.RedirectException;

public class UnitaOperativaNonTrovataException extends RedirectException{

	public UnitaOperativaNonTrovataException(String location) {
		super(location);
	}
	
	public UnitaOperativaNonTrovataException(String location, String message) {
		super(location,message);
	}
	
	public UnitaOperativaNonTrovataException(String location,Throwable t) {
		super(location, t);
	}
	
	public UnitaOperativaNonTrovataException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
