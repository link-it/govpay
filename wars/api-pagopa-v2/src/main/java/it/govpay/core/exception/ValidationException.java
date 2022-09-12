package it.govpay.core.exception;

public class ValidationException extends Exception {

	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public ValidationException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}
	public ValidationException(String msg) {
		super(msg);
	}

}