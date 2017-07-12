package it.govpay.model;

import java.util.List;

public interface IAutorizzato {
	
	public List<Acl> getAcls(); 
	public void setAcls(List<Acl> acls);

}
