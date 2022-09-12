package it.govpay.core.exceptions;

public class PromemoriaException extends Exception {
	private static final long serialVersionUID = 1L; 
	
	public PromemoriaException(String message) {
		super(message);
	}
	
	public PromemoriaException(String message, Throwable cause) {
		super(message, cause);
	}
}
