package it.govpay.core.exceptions;

import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;

public class UnprocessableEntityException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public UnprocessableEntityException(String cause) {
		super("Richiesta non processabile", "422000", cause, CategoriaEnum.RICHIESTA);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 422;
	}
}

