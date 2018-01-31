package it.govpay.core.dao.pagamenti.exception;

public class RicevutaNonTrovataException extends Exception{

	public RicevutaNonTrovataException() {
		
	}
	public RicevutaNonTrovataException(String message) {
		super(message);
	}
	
	public RicevutaNonTrovataException(String message,Throwable t) {
		super(message, t);
	}
	
	public RicevutaNonTrovataException(Throwable t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
