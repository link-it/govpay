/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class PagamentoPatchDTO extends AbstractPatchDTO {

	private String idSessione;
	
	public PagamentoPatchDTO(IAutorizzato user) {
		super(user);
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}

	
}
