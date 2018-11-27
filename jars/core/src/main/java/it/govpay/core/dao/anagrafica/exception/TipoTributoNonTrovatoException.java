package it.govpay.core.dao.anagrafica.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;


public class TipoTributoNonTrovatoException extends NonTrovataException{

	public TipoTributoNonTrovatoException(String message) {
		super(message);
	}
	public TipoTributoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
