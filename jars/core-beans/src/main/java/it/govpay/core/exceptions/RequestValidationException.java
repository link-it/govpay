package it.govpay.core.exceptions;

public class RequestValidationException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public RequestValidationException(String cause) {
		super("Richiesta non valida", "400000", cause, CategoriaEnum.RICHIESTA);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 400;
	}

}
