package it.govpay.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Autorizzato implements IAutorizzato {
	
	private List<Acl> acls;
	
	public Autorizzato() {
		acls = new ArrayList<Acl>();
	}
	
	public List<Acl> getAcls() {
		return acls;
	}
	
	public void addAllAcls(Collection<Acl> acls) {
		this.acls.addAll(acls);
	}

}
