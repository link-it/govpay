package it.govpay.core.rs.v1.beans.pagamenti;

import it.govpay.core.rs.v1.beans.JSONSerializable;

public class PagamentiPortaleResponseOk extends JSONSerializable {
	
	private String id;
	private String location;
	private String redirect;
	private String idSession;
	
	public String getIdSession() {
		return idSession;
	}
	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRedirect() {
		return redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortaleResponse";
	}
}
