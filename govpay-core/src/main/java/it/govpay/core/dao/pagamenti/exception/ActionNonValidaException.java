package it.govpay.core.dao.pagamenti.exception;

public class ActionNonValidaException extends RedirectException{

	public ActionNonValidaException(String location) {
		super(location);
	}
	
	public ActionNonValidaException(String location, String message) {
		super(message,location);
	}
	
	public ActionNonValidaException(String location,Throwable t) {
		super(location, t);
	}
	
	public ActionNonValidaException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
