/**
 * 
 */
package it.govpay.core.dao.eventi.exception;

import it.govpay.core.dao.commons.exception.NonTrovataException;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class EventoNonTrovatoException extends NonTrovataException {

	private static final long serialVersionUID = 1L;
	public EventoNonTrovatoException(String msg) {
		super(msg);
	}
	public EventoNonTrovatoException(String message, Throwable t) {
		super(message, t);
	}


}
