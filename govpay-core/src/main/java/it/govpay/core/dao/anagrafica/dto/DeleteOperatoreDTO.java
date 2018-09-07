/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class DeleteOperatoreDTO extends BasicCreateRequestDTO {


	public DeleteOperatoreDTO(IAutorizzato user) {
		super(user);
	}
	
	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	private String principal;
	
}
