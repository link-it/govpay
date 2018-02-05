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
public class PendenzaNonTrovataException extends Exception {

	private static final long serialVersionUID = 1L;

	public PendenzaNonTrovataException() {
		super();
	}
	
	public PendenzaNonTrovataException(String msg) {
		super(msg);
	}
	
	public PendenzaNonTrovataException(Throwable t) {
		super(t);
	}
	
	public PendenzaNonTrovataException(String msg, Throwable t) {
		super(msg, t);
	}
	

}
