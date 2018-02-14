package it.govpay.rs.v1.beans;

public class PagamentiPortaleResponseOk extends JSONSerializable {

	private long id;
	private String location;
	private String redirect;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
