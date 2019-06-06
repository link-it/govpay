package it.govpay.core.exceptions;

public class ResponseValidationException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public ResponseValidationException(String cause) {
		super("Risposta non valida", "403000", cause, CategoriaEnum.INTERNO);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 403;
	}

}
