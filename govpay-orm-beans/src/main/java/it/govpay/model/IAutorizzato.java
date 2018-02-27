package it.govpay.model;

import java.util.List;

public interface IAutorizzato {
	
	public void setPrincipal(String principal);
	public String getPrincipal();
	
	public void setRuoli(List<String> ruoli);
	public List<String> getRuoli();
	
	public List<Long> getIdDominio();
	public void setIdDominio(List<Long> idDominio) ;
	public List<Long> getIdTributo();
	public void setIdTributo(List<Long> idTributo);
	
	public List<Acl> getAcls(); 
	public void setAcls(List<Acl> acls);

}
