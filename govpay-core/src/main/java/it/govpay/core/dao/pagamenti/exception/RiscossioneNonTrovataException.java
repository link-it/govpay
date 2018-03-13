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
public class RiscossioneNonTrovataException extends Exception {

	private static final long serialVersionUID = 1L;

	public RiscossioneNonTrovataException() {
		super();
	}
	
	public RiscossioneNonTrovataException(String msg) {
		super(msg);
	}
	
	public RiscossioneNonTrovataException(Throwable t) {
		super(t);
	}
	
	public RiscossioneNonTrovataException(String msg, Throwable t) {
		super(msg, t);
	}
	

}
