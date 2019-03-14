package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Acl;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiAclDTOResponse {

	private Acl acl;

	public Acl getAcl() {
		return this.acl;
	}

	public void setAcl(Acl acl) {
		this.acl = acl;
	}
}
