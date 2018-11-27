package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

public class PagamentoPortaleNonTrovatoException extends NonTrovataException{

	private static final long serialVersionUID = 1L;

	public PagamentoPortaleNonTrovatoException(String msg) {
		super(msg);
	}
	public PagamentoPortaleNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

}
