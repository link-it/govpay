/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class AbstractPatchDTO extends BasicCreateRequestDTO {

	private List<PatchOp> op;
	
	public AbstractPatchDTO(IAutorizzato user) {
		super(user);
	}
	public List<PatchOp> getOp() {
		return op;
	}

	public void setOp(List<PatchOp> op) {
		this.op = op;
	}
	
}
