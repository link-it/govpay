package it.govpay.core.dao.pagamenti.exception;

public class TokenWISPNonValidoException extends RedirectException{

	public TokenWISPNonValidoException(String location) {
		super(location);
	}
	
	public TokenWISPNonValidoException(String location, String message) {
		super(message,location);
	}
	
	public TokenWISPNonValidoException(String location,Throwable t) {
		super(location, t);
	}
	
	public TokenWISPNonValidoException(String location, String message ,Throwable t) {
		super(location,message,t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
