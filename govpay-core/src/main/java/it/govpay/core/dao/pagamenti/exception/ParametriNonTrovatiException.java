package it.govpay.core.dao.pagamenti.exception;

public class ParametriNonTrovatiException extends RedirectException{

	public ParametriNonTrovatiException(String location) {
		super(location);
	}
	
	public ParametriNonTrovatiException(String location, String message) {
		super(message,location);
	}
	
	public ParametriNonTrovatiException(String location,Throwable t) {
		super(location, t);
	}
	
	public ParametriNonTrovatiException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
