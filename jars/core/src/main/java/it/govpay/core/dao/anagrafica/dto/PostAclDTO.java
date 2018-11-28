package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.model.Acl;

public class PostAclDTO extends BasicCreateRequestDTO  {
	
	private Acl acl;
	
	public PostAclDTO(Authentication user) {
		super(user);
	}

	public Acl getAcl() {
		return this.acl;
	}

	public void setAcl(Acl acl) {
		this.acl = acl;
	}

}
