package it.govpay.core.dao.pagamenti.dto;

public class PagamentiPortaleDTOResponse {

	private String id;
	private String idSessione;
	private String location;
	private String redirectUrl;
	private String idSessionePsp;
	private String idCarrelloRpt;
	
	public String getIdSessionePsp() {
		return idSessionePsp;
	}
	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}
	public String getIdCarrelloRpt() {
		return idCarrelloRpt;
	}
	public void setIdCarrelloRpt(String idCarrelloRpt) {
		this.idCarrelloRpt = idCarrelloRpt;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRedirectUrl() {
		return this.redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getIdSessione() {
		return this.idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	
}
