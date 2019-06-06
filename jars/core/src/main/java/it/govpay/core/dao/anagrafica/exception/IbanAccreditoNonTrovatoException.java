package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class IbanAccreditoNonTrovatoException extends NonTrovataException{

	public IbanAccreditoNonTrovatoException(String message) {
		super(message);
	}
	public IbanAccreditoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
