package it.govpay.core.exceptions;

import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;

public class RequestValidationException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public RequestValidationException(String cause) {
		super("Richiesta non valida", "403000", cause, CategoriaEnum.SINTASSI);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 403;
	}

}
