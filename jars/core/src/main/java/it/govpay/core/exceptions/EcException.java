package it.govpay.core.exceptions;

public class EcException extends BaseExceptionV1{
	

	public EcException(String cause) {
		super("Errore ente creditore", "502000", cause, CategoriaEnum.EC);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getTransportErrorCode() {
		return 502;
	}

}
