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
public class IncassoNonTrovatoException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncassoNonTrovatoException() {
		super();
	}
	
	public IncassoNonTrovatoException(String msg) {
		super(msg);
	}
	
	public IncassoNonTrovatoException(Throwable t) {
		super(t);
	}
	
	public IncassoNonTrovatoException(String msg, Throwable t) {
		super(msg, t);
	}
	

}
