package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class DominioNonTrovatoException extends NonTrovataException{

	public DominioNonTrovatoException(String message) {
		super(message);
	}
	public DominioNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	private static final long serialVersionUID = 1L;

}
