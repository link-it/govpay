package it.govpay.model;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Utenza.TIPO_UTENZA;


public interface IAutorizzato {
	
	public void setCheckSubject(boolean checkSubject);
	public boolean isCheckSubject();
	
	public void setPrincipal(String principal);
	public String getPrincipal();
	
	public void setPrincipalOriginale(String subject);
	public String getPrincipalOriginale();
	
	public void setRuoli(List<String> ruoli);
	public List<String> getRuoli();
	
	public List<String> getIdDominio();
	public List<Long> getIdDomini();
//	public void setIdDominio(List<String> idDominio) ;
	public List<String> getIdTributo();
	public List<Long> getIdTributi();
//	public void setIdTributo(List<String> idTributo);
	
	public List<Acl> getAcls(); 
//	public void setAcls(List<Acl> acls);

	public void merge(IAutorizzato src) throws ServiceException; 
	
	public TIPO_UTENZA getTipoUtenza();
	public String getIdentificativo();
}
