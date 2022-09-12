package it.govpay.core.exception;

public class NotAuthorizedException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public NotAuthorizedException(String cause) {
		super("Operazione non autorizzata", "403000", cause, CategoriaEnum.AUTORIZZAZIONE);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 403;
	}

}
