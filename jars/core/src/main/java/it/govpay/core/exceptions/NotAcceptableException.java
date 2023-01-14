package it.govpay.core.exceptions;

public class NotAcceptableException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public NotAcceptableException(String cause) {
		super("Risorsa non disponibile nel formato richiesto", "406000", cause, CategoriaEnum.RICHIESTA);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 406;
	}
}

