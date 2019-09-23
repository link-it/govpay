package it.govpay.core.utils.trasformazioni.exception;

/**
 * 
 * @author pintori
 *
 */
public class TrasformazioneException extends Exception {

	public TrasformazioneException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public TrasformazioneException(Throwable cause)
	{
		super(cause);
	}
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public TrasformazioneException() {
		super();
	}
	public TrasformazioneException(String msg) {
		super(msg);
	}

}
