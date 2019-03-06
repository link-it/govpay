package it.govpay.core.exceptions;

public class UnprocessableEntityException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public UnprocessableEntityException(String cause) {
		super("Richiesta non processabile", "400000", cause, CategoriaEnum.RICHIESTA);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 400;
	}
}

