package it.govpay.core.dao.pagamenti.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

/**
 * @author Giuliano Pintori (pintori@link.it)
 * @author  $Author: pintori $
 * 
 */
public class AllegatoNonTrovatoException extends NonTrovataException {

	private static final long serialVersionUID = 1L;
	public AllegatoNonTrovatoException(String msg) {
		super(msg);
	}
	public AllegatoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}


}
