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
public class RendicontazioneNonTrovataException extends Exception {

	private static final long serialVersionUID = 1L;

	public RendicontazioneNonTrovataException() {
		super();
	}
	
	public RendicontazioneNonTrovataException(String msg) {
		super(msg);
	}
	
	public RendicontazioneNonTrovataException(Throwable t) {
		super(t);
	}
	
	public RendicontazioneNonTrovataException(String msg, Throwable t) {
		super(msg, t);
	}
	

}
