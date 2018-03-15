/**
 * 
 */
package it.govpay.core.dao.commons.exception;

import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 15 mar 2018 $
 * 
 */
public class NonTrovataException extends GovpayRsException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonTrovataException(String codice) {
		super(codice, 404, CategoriaEnum.OPERAZIONE);
	}

	public NonTrovataException(String codice, String message) {
		super(codice, 404, CategoriaEnum.OPERAZIONE, message);
	}

	public NonTrovataException(String codice, String message, Throwable t) {
		super(codice, 404, CategoriaEnum.OPERAZIONE, message, t);
	}

	public NonTrovataException(String codice, Throwable t) {
		super(codice, 404, CategoriaEnum.OPERAZIONE, t);
	}

}
