package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class TipoVersamentoNonTrovatoException extends NonTrovataException{

	public TipoVersamentoNonTrovatoException(String message) {
		super(message);
	}
	public TipoVersamentoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
