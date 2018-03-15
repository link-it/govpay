/**
 * 
 */
package it.govpay.core.dao.commons.exception;

import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 15 mar 2018 $
 * 
 */
public class NonTrovataException extends BaseExceptionV1{

	private static final long serialVersionUID = 1L;
	
	public NonTrovataException(String cause) {
		super("Risorsa non trovata", "404000", cause, CategoriaEnum.OPERAZIONE);
	}
	
	public NonTrovataException(String cause, Throwable t) {
		super("Risorsa non trovata", "404000", cause, CategoriaEnum.OPERAZIONE);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 404;
	}

}
