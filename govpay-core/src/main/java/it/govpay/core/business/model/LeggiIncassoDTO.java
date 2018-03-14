package it.govpay.core.business.model;

public class LeggiIncassoDTO {
	
	private String principal;
	private String idDominio;
	private String idIncasso;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdIncasso() {
		return idIncasso;
	}
	public void setIdIncasso(String idIncasso) {
		this.idIncasso = idIncasso;
	}

}
