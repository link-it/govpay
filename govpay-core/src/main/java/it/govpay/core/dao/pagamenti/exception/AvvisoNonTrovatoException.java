/**
 * 
 */
package it.govpay.core.dao.pagamenti.exception;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class AvvisoNonTrovatoException extends Exception {

	private static final long serialVersionUID = 1L;

	public AvvisoNonTrovatoException() {
		super();
	}
	
	public AvvisoNonTrovatoException(String msg) {
		super(msg);
	}
	
	public AvvisoNonTrovatoException(Throwable t) {
		super(t);
	}
	
	public AvvisoNonTrovatoException(String msg, Throwable t) {
		super(msg, t);
	}
	

}
