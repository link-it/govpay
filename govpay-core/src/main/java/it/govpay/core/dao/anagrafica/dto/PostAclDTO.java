package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;

public class PostAclDTO extends BasicCreateRequestDTO  {
	
	private Acl acl;
	
	public PostAclDTO(IAutorizzato user) {
		super(user);
	}

	public Acl getAcl() {
		return acl;
	}

	public void setAcl(Acl acl) {
		this.acl = acl;
	}

}
