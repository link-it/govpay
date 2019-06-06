/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiAclDTO extends BasicCreateRequestDTO {


	public LeggiAclDTO(Authentication user) {
		super(user);
	}
	
	private Long idAcl;

	public Long getIdAcl() {
		return this.idAcl;
	}

	public void setIdAcl(Long idAcl) {
		this.idAcl = idAcl;
	}
	
}
