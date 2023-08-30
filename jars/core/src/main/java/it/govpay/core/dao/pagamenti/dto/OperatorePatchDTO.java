/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class OperatorePatchDTO extends AbstractPatchDTO {

	private String idOperatoreToPatch;
	
	public OperatorePatchDTO(Authentication user) {
		super(user);
	}

	public String getIdOperatoreToPatch() {
		return this.idOperatoreToPatch;
	}

	public void setIdOperatoreToPatch(String idOperatoreToPatch) {
		this.idOperatoreToPatch = idOperatoreToPatch;
	}

	
}
