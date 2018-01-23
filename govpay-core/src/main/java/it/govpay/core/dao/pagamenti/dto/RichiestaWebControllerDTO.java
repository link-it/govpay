package it.govpay.core.dao.pagamenti.dto;

public class RichiestaWebControllerDTO {

	private String idSessione = null;
	private String principal = null;
	private String action = null;
	private String type = null;
	private String wispDominio = null;
	private String wispKeyPA = null;
	private String wispKeyWisp = null;
	
	
	public String getIdSessione() {
		return idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWispDominio() {
		return wispDominio;
	}
	public void setWispDominio(String wispDominio) {
		this.wispDominio = wispDominio;
	}
	public String getWispKeyPA() {
		return wispKeyPA;
	}
	public void setWispKeyPA(String wispKeyPA) {
		this.wispKeyPA = wispKeyPA;
	}
	public String getWispKeyWisp() {
		return wispKeyWisp;
	}
	public void setWispKeyWisp(String wispKeyWisp) {
		this.wispKeyWisp = wispKeyWisp;
	} 
	
}
