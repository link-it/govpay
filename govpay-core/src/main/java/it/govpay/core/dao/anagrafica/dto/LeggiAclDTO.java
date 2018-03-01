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
public class LeggiAclDTO extends BasicCreateRequestDTO {


	public LeggiAclDTO(IAutorizzato user) {
		super(user);
	}
	
	private Long idAcl;

	public Long getIdAcl() {
		return idAcl;
	}

	public void setIdAcl(Long idAcl) {
		this.idAcl = idAcl;
	}
	
}
